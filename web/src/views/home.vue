<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
              mode="inline"
              :style="{ height: '100%', borderRight: 0 }"
      >
        <a-menu-item key="welcome">
          <router-link :to="'/'">
            <MailOutlined />
            <span>欢迎</span>
          </router-link>
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id">
          <template v-slot:title>
            <span><user-putlined />{{item.name}}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutlined /><span>{{child.name}}</span>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-list item-layout="vertical"
              size="large"
              :data-source="ebooks"
              :grid="{ gutter: 20, column: 3 }">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span v-for="{ type, text } in actions" :key="type">
                <component v-bind:is="type" style="margin-right: 8px" />
                {{ text }}
              </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.name }}</a>
              </template>
              <template #avatar><a-avatar :src="item.cover" /></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios'
import {Tool} from "@/util/tool";
import {message} from "ant-design-vue";

export default defineComponent({
  name: 'Home',
  setup() {
    const ebooks = ref()
    const actions: Record<string, string>[] = [
      { type: 'StarOutlined', text: '156' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '2' },
    ];

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

    onMounted(() => {
      axios.get("/ebook/list",{
        params: {
          page: 1,
          size: 1000
        }
      }).then((resp) => {
        const data = resp.data
        ebooks.value = data.content.list
      })
      handleQueryCategory()
    })

    return {
      ebooks,
      actions,

      level1,
      handleQueryCategory
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
</style>
