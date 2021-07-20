<template>
  <a-layout>
    <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="6">
          <a-tree
            v-if="level1.length > 0"
            :tree-data="level1"
            @select="onSelect"
            :replace-fields="{title: 'name', key: 'id', value: 'id'}"
            :default-expand-all="true"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">

        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import {useRoute} from "vue-router";
  import {defineComponent, onMounted, ref} from 'vue';
  import axios from 'axios'
  import {Tool} from "@/util/tool";
  import {message} from "ant-design-vue";

  export default {
    name: "doc",
    setup() {
      const route = useRoute()
      const docs = ref()

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
          } else {
            message.error(data.message);
          }
        })
      }

      onMounted(() => {
        handleQuery()
      })

      return {
        level1
      }
    }
  }
</script>

<style scoped>

</style>