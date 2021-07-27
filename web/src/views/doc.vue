<template>
  <a-layout>
    <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <h3 v-if="level1.length === 0">对不起，找不到相关文档！</h3>
      <a-row :gutter="24">
        <a-col :span="6">
          <a-tree
            v-if="level1.length > 0"
            :tree-data="level1"
            @select="onSelect"
            :replace-fields="{title: 'name', key: 'id', value: 'id'}"
            :default-expand-all="true"
            :defaultSelectedKeys="defaultSelectedKeys"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">
          <div>
            <h2>{{doc.name}}</h2>
            <div>
              <span>阅读数: {{doc.viewCount}}</span> &nbsp; &nbsp;
              <span>点赞数: {{doc.voteCount}}</span>
            </div>
            <a-divider style="height: 2px; background-color: #9999cc" />
          </div>
          <div class="wang-editor" :innerHTML="html"></div>
          <div class="vote-div">
            <a-button type="primary" shape="round" :size="'large'" @click="vote">
              <template #icon><LikeOutlined /> &nbsp;点赞: {{doc.voteCount}}</template>
            </a-button>
          </div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
  import {useRoute} from "vue-router";
  import {defineComponent, onMounted, ref} from 'vue';
  import axios from 'axios'
  import {Tool} from "@/util/tool";
  import {message} from "ant-design-vue";

  export default defineComponent({
    name: "doc",
    setup() {
      const route = useRoute()
      const docs = ref()
      const defaultSelectedKeys = ref()
      defaultSelectedKeys.value = []
      //  当前选中的文档
      const doc = ref()
      doc.value = {}

      /**
       * 查询所有文档
       **/
      const level1 = ref()
      level1.value = []
      const handleQuery = () => {
        axios.get("/doc/all/" + route.query.ebookId).then((resp) => {
          const data = resp.data;
          if (data.success) {
            docs.value = data.content;
            level1.value = []
            level1.value = Tool.array2Tree(docs.value, 0)

            if (Tool.isNotEmpty(level1)) {
              // 把节点设置成选中状态，但是并不会调用onSelect方法，所以要自己手动查询
              defaultSelectedKeys.value = [level1.value[0].id]
              handleQueryContent(defaultSelectedKeys.value)
              //  初始显示文档信息
              doc.value = level1.value[0]
            }
          } else {
            message.error(data.message);
          }
        })
      }

      /**
       * 查询富文本内容
       **/
      const html = ref()
      const handleQueryContent = (id: string) => {
        axios.get("/doc/find-content/" + id).then((resp) => {
          const data = resp.data;
          if (data.success) {
            // 渲染富文本内容
            html.value = data.content
          } else {
            message.error(data.message);
          }
        })
      }

      /**
       * 选中某一节点时
       * @param selectedKeys
       * @param info
       */
      const onSelect = (selectedKeys: any, info: any) => {
        if (Tool.isNotEmpty(selectedKeys)) {
          //  选中某一节点时，加载该节点的文档信息
          doc.value = info.selectedNodes[0].props;
          //  加载内容
          handleQueryContent(selectedKeys[0])
        }
      }

      /**
       * 点赞
       */
      const vote = () => {
        axios.get('/doc/vote/' +  doc.value.id).then((resp) => {
          const data = resp.data
          if (data.success) {
            doc.value.voteCount++
          } else {
            message.error(data.message)
          }
        })
      }

      onMounted(() => {
        handleQuery()
      })

      return {
        level1,
        html,
        defaultSelectedKeys,
        doc,
        onSelect,
        vote
      }
    }
  })
</script>

<!--没有加scoped的style是全局的-->
<style>
  /* wangeditor默认样式， 参照https://www.wangeditor.com/doc/pages/02-%E5%86%85%E5%AE%B9%E5%A4%84%E7%90%86/03-%E8%8E%B7%E5%8F%96html.html */
  .wang-editor table {
    border-top: 1px solid #ccc;
    border-left: 1px solid #ccc;
  }
  .wang-editor table td,
  .wang-editor table th {
    border-bottom: 1px solid #ccc;
    border-right: 1px solid #ccc;
    padding: 3px 5px;
  }
  .wang-editor table th {
    border-bottom: 2px solid #ccc;
    text-align: center;
  }

  /* blockquote 样式 */
  .wang-editor blockquote {
    display: block;
    border-left: 8px solid #d0e5f2;
    padding: 5px 10px;
    margin: 10px 0;
    line-height: 1.4;
    font-size: 100%;
    background-color: #f1f1f1;
  }

  /* blockquote 样式 */
  .wang-editor blockquote p{
    font-family: "YouYuan";
    margin: 20px 10px !important;
    font-size: 16px !important;
    font-weight: 600;
  }

  /* code 样式 */
  .wang-editor code {
    display: inline-block;
    *display: inline;
    *zoom: 1;
    background-color: #f1f1f1;
    border-radius: 3px;
    padding: 3px 5px;
    margin: 0 3px;
  }
  .wang-editor pre code {
    display: block;
  }

  /* ul ol 样式 */
  .wang-editor ul, ol {
    margin: 10px 0 10px 20px;
  }
  /* 图片自适应 */
  .wang-editor img {
    max-width: 100%;
    height: auto;
  }

  .vote-div {
    padding: 15px;
    text-align: center;
  }
</style>