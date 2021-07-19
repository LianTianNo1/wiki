<template>
  <a-layout>
    <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
          <p>
            <a-form :model="param" layout="inline">
              <a-form-item>
                <a-button type="primary" @click="handleQuery()">
                  查询
                </a-button>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="add()">
                  新增
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-table
            :columns="columns"
            :row-key="record => record.id"
            :data-source="level1"
            :loading="loading"
            :pagination="false"
            v-if="level1.length > 0"
            :default-expand-all-rows="true"
            size="small"
          >
            <template #name="{ text, record }">
              {{record.sort}} {{text}}
            </template>
            <template v-slot:action="{ text, record }">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  编辑
                </a-button>
                <a-popconfirm
                  title="删除后不可恢复，确认删除？"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="showConfirm(record.id)"
                >
                  <a-button type="primary" danger size="small">
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical">
            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="请输入名称"/>
            </a-form-item>
            <a-form-item>
              <a-tree-select
                v-model:value="doc.parent"
                style="width: 100%"
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                :tree-data="treeSelectData"
                placeholder="请选择父文档"
                tree-default-expand-all
                :replaceFields="{title: 'name', key: 'id', value: 'id'}"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="请输入顺序"/>
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
  import {defineComponent, onMounted, ref, createVNode} from 'vue';
  import axios from 'axios';
  import {message} from "ant-design-vue";
  import {Tool} from "@/util/tool";
  import {useRoute} from "vue-router";
  import { Modal } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import E from 'wangeditor'

  export default defineComponent({
    name: 'AdminDoc',
    setup() {
      const editor = new E('#content')
      const param = ref()
      param.value = {}
      const docs = ref();
      const loading = ref(false);

      editor.config.zIndex = 0

      const columns = [
        {
          title: '名称',
          dataIndex: 'name',
          slots: { customRender: 'name' }
        },
        {
          title: 'Action',
          key: 'action',
          slots: {customRender: 'action'}
        }
      ];

      /**
       * 查询所有文档
       **/
      const level1 = ref()
      level1.value = []
      const handleQuery = () => {
        loading.value = true;
        axios.get("/doc/all").then((resp) => {
          loading.value = false;
          const data = resp.data;
          if (data.success) {
            docs.value = data.content;
            level1.value = []
            level1.value = Tool.array2Tree(docs.value, 0)
          } else {
            message.error(data.message);
          }
        })
      }

      // ---------- 表单 -----------
      // 因为树选择组件的属性状态，会随当前编辑的节点而变化，所以单独声明一个响应式变量
      const treeSelectData = ref()
      treeSelectData.value = []
      const doc = ref({})
      const modalLoading = ref(false)
      const handleSave = () => {
        modalLoading.value = true
        axios.post("/doc/save", doc.value).then((resp) => {
          modalLoading.value = false
          const data = resp.data
          if (data.success) {

            // 重新加载列表
            handleQuery()
          } else {
            message.error(data.message)
          }
        });
      }

      /**
       * 将某节点及其子孙节点全部置为disabled
       */
      const setDisable = (treeSelectData: any, id: any) => {
        for (let i = 0; i < treeSelectData.length; i++) {
          const node = treeSelectData[i]
          if (node.id === id) {
            node.disabled = true

            const children = node.children
            if (Tool.isNotEmpty(children)) {
              for (let j = 0; j < children.length; j++) {
                setDisable(children, children[j].id)
              }
            }
          } else {
            const children = node.children
            if (Tool.isNotEmpty(children)) {
              setDisable(children, id)
            }
          }
        }
      }

      /**
       * 查找整根树枝
       */
      let ids: Array<string> = []
      let idsName: Array<string> = []
      const getDeleteIds = (treeSelectData: any, id: any) => {
        for (let i = 0; i < treeSelectData.length; i++) {
          const node = treeSelectData[i]
          if (node.id === id) {
            ids.push(node.id)
            idsName.push(node.name)

            const children = node.children
            if (Tool.isNotEmpty(children)) {
              for (let j = 0; j < children.length; j++) {
                getDeleteIds(children, children[j].id)
              }
            }
          } else {
            const children = node.children
            if (Tool.isNotEmpty(children)) {
              getDeleteIds(children, id)
            }
          }
        }
      }

      /**
       * 编辑
       */
      const edit = (record: any) => {
        doc.value = Tool.copy(record)

        treeSelectData.value = Tool.copy(level1.value)
        setDisable(treeSelectData.value, record.id)

        treeSelectData.value.unshift({id: 0, name: '无'})

      }

      /**
       * 新增
       */
      const route = useRoute()
      const add = () => {
        doc.value = {
          ebookId: route.query.ebookId
        }

        treeSelectData.value = Tool.copy(level1.value)

        treeSelectData.value.unshift({id: 0, name: '无'})
      }

      /**
       * 删除
       */
      const handleDelete = (id: string) => {
        // join() 方法将一个数组（或一个类数组对象）的所有元素连接成一个字符串并返回这个字符串
        axios.delete("/doc/delete/" + ids.join(",")).then((resp) => {
          const data = resp.data
          if (data.success) {
            // 重新加载列表
            handleQuery()
          }
        })
      }

      const showConfirm = (id: string) => {
        ids = []
        idsName = []
        getDeleteIds(level1.value, id)
        Modal.confirm({
          title: '重要提醒',
          icon: createVNode(ExclamationCircleOutlined),
          content: '将删除: 【'+ idsName +'】。删除后不可恢复，确认删除？',
          onOk() {
            handleDelete(id)
          },
          // eslint-disable-next-line @typescript-eslint/no-empty-function
          onCancel() {},
        })
      }

      onMounted(() => {
        handleQuery()
        editor.create()
      })

      return {
        //docs,
        level1,
        columns,
        loading,
        param,
        handleQuery,

        doc,
        modalLoading,
        treeSelectData,
        edit,
        add,
        handleSave,
        handleDelete,
        showConfirm
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
