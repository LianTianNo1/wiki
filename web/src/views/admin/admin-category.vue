<template>
  <a-layout>
    <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form :model="param" layout="inline">
          <a-form-item>
            <a-button type="primary" @click="handleQuery()" size="large">
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
        :data-source="categories"
        :loading="loading"
        :pagination="false"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar"/>
        </template>
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
    title="分类表单"
    v-model:visible="modalVisible"
    :confirm-loading="modalLoading"
    @ok="handleModalOk"
  >
    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="父分类">
        <a-input v-model:value="category.parent" />
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="category.sort" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
  import {defineComponent, onMounted, ref} from 'vue';
  import axios from 'axios';
  import {message} from "ant-design-vue";
  import {Tool} from "@/util/tool";

  export default defineComponent({
    name: 'AdminCategory',
    setup() {
      const param = ref()
      param.value = {}
      const categories = ref();
      const loading = ref(false);

      const columns = [
        {
          title: '名称',
          dataIndex: 'name'
        },
        {
          title: '父分类',
          key: 'parent',
          dataIndex: 'parent'
        },
        {
          title: '顺序',
          dataIndex: 'sort'
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
      const handleQuery = () => {
        loading.value = true;
        axios.get("/category/all").then((resp) => {
          loading.value = false;
          const data = resp.data;
          if (data.success) {
            categories.value = data.content;
          } else {
            message.error(data.message);
          }
        });
      };

      // ---------- 表单 -----------
      const category = ref({})
      const modalVisible = ref(false)
      const modalLoading = ref(false)
      const handleModalOk = () => {
        modalLoading.value = true
        axios.post("/category/save", category.value).then((resp) => {
          modalLoading.value = false
          const data = resp.data
          if (data.success) {
            modalVisible.value = false

            // 重新加载列表
            handleQuery()
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
        category.value = Tool.copy(record)
      }

      /**
       * 新增
       */
      const add = () => {
        modalVisible.value = true
        category.value = {}
      }

      /**
       * 删除
       */
      const handleDelete = (id: string) => {
        axios.delete("/ebook/delete/" + id).then((resp) => {
          const data = resp.data
          if (data.success) {
            // 重新加载列表
            handleQuery()
          }
        })
      }

      onMounted(() => {
        handleQuery();
      });

      return {
        categories,
        columns,
        loading,
        param,
        handleQuery,

        category,
        modalVisible,
        modalLoading,
        edit,
        add,
        handleModalOk,
        handleDelete
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
