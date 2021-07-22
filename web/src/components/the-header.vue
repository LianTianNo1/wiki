<template>
  <a-layout-header class="header">
    <div class="logo"/>
    <a-row type="flex">
      <a-col :span="20">
        <a-menu
          theme="dark"
          class="menu"
          mode="horizontal"
          :style="{ lineHeight: '64px' }"
        >
          <a-menu-item key="/">
            <router-link to="/">首页</router-link>
          </a-menu-item>
          <a-menu-item key="/admin/user" v-if="!!user.id">
            <router-link to="/admin/user">用户管理</router-link>
          </a-menu-item>
          <a-menu-item key="/admin/ebook" v-if="!!user.id">
            <router-link to="/admin/ebook">电子书管理</router-link>
          </a-menu-item>
          <a-menu-item key="/admin/category" v-if="!!user.id">
            <router-link to="/admin/category">分类管理</router-link>
          </a-menu-item>
          <a-menu-item key="/about">
            <router-link to="/about">关于我</router-link>
          </a-menu-item>
        </a-menu>
      </a-col>
      <a-col :span="4">
        <a v-show="!!user.id" style="padding-right: 20px;color: white">
          <span>你好：{{user.name}} </span>
        </a>
        <a v-show="!user.id" @click="showLoginModal" style="color: white">
          <span>登录</span>
        </a>
        <a-popconfirm
          title="是否确认退出登录？"
          ok-text="是"
          cancel-text="否"
          @confirm="logout"
        >
          <a v-show="!!user.id" style="color: white">
            <span>退出登录</span>
          </a>
        </a-popconfirm>
      </a-col>
    </a-row>
  </a-layout-header>

  <a-modal
    title="登录"
    v-model:visible="loginModalVisible"
    :confirm-loading="loginModalLoading"
    @ok="login"
  >
    <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="登录名">
        <a-input v-model:value="loginUser.loginName" />
      </a-form-item>
      <a-form-item label="密码">
        <a-input v-model:value="loginUser.password" type="password"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
  import {defineComponent, onMounted, ref, computed} from 'vue';
  import axios from 'axios';
  import {message} from "ant-design-vue";
  import {Tool} from "@/util/tool";
  import store from "@/store";

  declare let hexMd5: any
  declare let KEY: any

  export default defineComponent({
    name: 'the-header',
    setup() {
      //  用于提交登录信息
      const loginUser = ref({
        loginName: 'test',
        password: 'test'
      })
      //  用于保存登录后的获取的信息, computed会监听store的变化
      const user = computed(() => store.state.user)

      // ---------- 登录表单 -----------

      const loginModalVisible = ref(false)
      const loginModalLoading = ref(false)

      /**
       * 登录
       */
      const login = () => {
        loginModalLoading.value = true
        loginUser.value.password = hexMd5(loginUser.value.password + KEY)
        axios.post('user/login', loginUser.value).then((resp) => {
          loginModalLoading.value = false
          const data = resp.data
          if (data.success) {
            loginModalVisible.value = false
            store.commit('setUser', data.content)
            message.success("登录成功！")
          } else {
            message.error(data.message)
          }
        })
      }

      /**
       * 显示登录模态框
       */
      const showLoginModal = () => {
        loginModalVisible.value = true
      }

      /**
       * 退出登录
       */
      const logout = () => {
        axios.get('user/logout/' + user.value.token).then((resp) => {
          const data = resp.data
          if (data.success) {
            message.success("退出登录成功！")
            // 为了避免空指针异常，所以用空对象
            store.commit('setUser', {})
          } else {
            message.error(data.message)
          }
        })
      }

      return {
        user,
        loginUser,
        loginModalVisible,
        loginModalLoading,
        login,
        showLoginModal,
        logout
      }
    }
  });
</script>

<style>
</style>