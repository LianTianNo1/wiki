<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
        @click="handleClick"
      >
        <a-menu-item key="welcome">
          <MailOutlined/>
          <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id">
          <template v-slot:title>
            <FolderOpenOutlined />
            <span>  {{item.name}}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <FileOutlined />
            <span>{{child.name}}</span>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="welcome" v-if="isShowWelcome">
        <the-welcome></the-welcome>
      </div>
      <a-list v-else
              item-layout="vertical"
              size="large"
              :data-source="ebooks"
              :grid="{ gutter: 20, column: 3 }">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span>
                <component v-bind:is="'FileOutlined'" style="margin-right: 8px"/>
                {{ item.docCount }}
              </span>
              <span>
                <component v-bind:is="'UserOutlined'" style="margin-right: 8px"/>
                {{ item.viewCount }}
              </span>
              <span>
                <component v-bind:is="'LikeOutlined'" style="margin-right: 8px"/>
                {{ item.voteCount }}
              </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?ebookId=' + item.id">
                  {{ item.name }}
                </router-link>
              </template>
              <template #avatar>
                <a-avatar :src="item.cover"/>
              </template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
  import {defineComponent, onMounted, ref} from 'vue';
  import axios from 'axios'
  import {Tool} from "@/util/tool";
  import {message} from "ant-design-vue";
  import TheWelcome from '@/components/the-welcome.vue'

  export default defineComponent({
    name: 'Home',
    components: {
      TheWelcome
    },
    setup() {
      const ebooks = ref()

      /**
       * 查询所有分类
       **/
      const level1 = ref()
      let categories: any
      const handleQueryCategory = () => {
        axios.get("/category/all").then((resp) => {
          const data = resp.data
          if (data.success) {
            categories = data.content
            level1.value = []
            level1.value = Tool.array2Tree(categories, 0)
          } else {
            message.error(data.message);
          }
        })
      }

      const handleQueryEbook = (category2Id: any) => {
        axios.get("/ebook/list", {
          params: {
            page: 1,
            size: 1000,
            category2Id: category2Id
          }
        }).then((resp) => {
          const data = resp.data
          ebooks.value = data.content.list
        })
      }

      const isShowWelcome = ref(true)

      const handleClick = (value: any) => {
        if (value.key === 'welcome') {
          isShowWelcome.value = true
        } else {
          isShowWelcome.value = false
          handleQueryEbook(value.key)
        }
      }

      onMounted(() => {

        handleQueryCategory()
      })

      return {
        ebooks,

        level1,
        handleQueryCategory,

        isShowWelcome,
        handleClick
      }
    }
  });
</script>

<style scoped>
  .ant-avatar {
    width: 50px;
    height: 50px;
    line-height: 50px;
    border-radius: 8%;
    margin: 5px 0;
  }

  /*.welcome {*/
  /*  margin-left: 35%;*/
  /*  margin-top: 5%;*/
  /*}*/
</style>
