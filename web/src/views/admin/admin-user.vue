<template>
  <a-layout>
    <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form :model="param" layout="inline">
          <a-form-item>
            <a-input v-model:value="param.loginName" placeholder="登录名" size="large"/>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})" size="large">
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()" size="large">
              新增
            </a-button>
          </a-form-item>
        </a-form>
      </p>
      <a-table
        :columns="columns"
        :row-key="record => record.id"
        :data-source="users"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
              title="删除后不可恢复，确认删除？"
              ok-text="是"
              cancel-text="否"
              @confirm="handleDelete(record.id)"
            >
              <a-button type="primary" danger>
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal
    title="用户信息"
    v-model:visible="modalVisible"
    :confirm-loading="modalLoading"
    @ok="handleModalOk"
  >
    <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="用户名">
        <a-input v-model:value="user.loginName" :disabled="!!user.id" />
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-model:value="user.name" />
      </a-form-item>
      <a-form-item label="密码">
        <a-input v-model:value="user.password" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
  import {defineComponent, onMounted, ref} from 'vue';
  import axios from 'axios';
  import {message} from "ant-design-vue";
  import {Tool} from "@/util/tool";

  // 告诉程序这两个值是存在的，因为使用的是ts，否则会报错
  declare let hexMd5: any
  declare let KEY: any

  export default defineComponent({
    name: 'AdminUser',
    setup() {
      const param = ref()
      param.value = {}
      const users = ref();
      const pagination = ref({
        current: 1,
        pageSize: 10,
        total: 0
      });
      const loading = ref(false);

      const columns = [
        {
          title: '用户名',
          dataIndex: 'loginName',
        },
        {
          title: '昵称',
          dataIndex: 'name'
        },
        {
          title: '密码',
          dataIndex: 'password'
        },
        {
          title: 'Action',
          key: 'action',
          slots: {customRender: 'action'}
        }
      ];

      /**
       * 数据查询
       **/
      const handleQuery = (params: any) => {
        loading.value = true;
        axios.get("/user/list", {
          params: {
            page: params.page,
            size: params.size,
            loginName: param.value.loginName
          }
        }).then((resp) => {
          loading.value = false;
          const data = resp.data;
          if (data.success) {
            users.value = data.content.list

            // 重置分页按钮
            pagination.value.current = params.page;
            pagination.value.total = data.content.total
          } else {
            message.error(data.message);
          }
        });
      };

      /**
       * 表格点击页码时触发
       */
      const handleTableChange = (pagination: any) => {
        console.log("看看自带的分页参数都有啥：" + pagination);
        handleQuery({
          page: pagination.current,
          size: pagination.pageSize
        });
      };

      // ---------- 表单 -----------
      const user = ref()
      const modalVisible = ref(false)
      const modalLoading = ref(false)
      const handleModalOk = () => {
        modalLoading.value = true

        // 对表单的明文密码进行加密，该方法在public/js/md5.js，然后在public/index.html中引入
        user.value.password = hexMd5(user.value.password + KEY)

        axios.post("/user/save", user.value).then((resp) => {
          modalLoading.value = false
          const data = resp.data
          if (data.success) {
            modalVisible.value = false

            // 重新加载列表
            handleQuery({
              page: pagination.value.current,
              size: pagination.value.pageSize
            })
          } else {
            message.error(data.message)
          }
        });
      }

      /**
       * 编辑
       */
      const edit = (record: any) => {
        modalVisible.value = true
        user.value = Tool.copy(record)
      }

      /**
       * 新增
       */
      const add = () => {
        modalVisible.value = true
        user.value = {}
      }

      /**
       * 删除
       */
      const handleDelete = (id: string) => {
        console.log('==========================' + id)
        axios.delete("/user/delete/" + id).then((resp) => {
          const data = resp.data
          if (data.success) {
            // 重新加载列表
            handleQuery({
              page: pagination.value.current,
              size: pagination.value.pageSize
            })
          }
        })
      }

      onMounted(() => {
        handleQuery({
            page: 1,
            size: pagination.value.pageSize
        })
      })

      return {
        users,
        pagination,
        columns,
        loading,
        param,
        handleTableChange,
        handleQuery,

        user,
        modalVisible,
        modalLoading,
        edit,
        add,
        handleModalOk,
        handleDelete,
      }
    }
  });
</script>

<style scoped>
  img {
    width: 50px;
    height: 50px;
  }
</style>
