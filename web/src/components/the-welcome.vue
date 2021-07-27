<template>
  <div>
    <a-row>
      <a-col :span="24">
        <a-card>
          <a-row>
            <a-col :span="8">
              <a-statistic title="总阅读量" :value="statistic.viewCount">
                <template #suffix>
                  <UserOutlined/>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="8">
              <a-statistic title="总点赞量" :value="statistic.voteCount">
                <template #suffix>
                  <likeOutlined/>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="8">
              <a-statistic title="点赞率" :value="statistic.voteCount / statistic.viewCount * 100"
                           :value-style="{color: '#cf1322'}"
                           suffix="%"
                           :precision="2">
                <template #suffix>
                  <likeOutlined/>
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
    <br>
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card>
          <a-row>
            <a-col :span="12">
              <a-statistic title="今日阅读" :value="statistic.todayViewCount">
                <template #suffix>
                  <UserOutlined/>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="12">
              <a-statistic title="今日点赞" :value="statistic.todayVoteCount">
                <template #suffix>
                  <likeOutlined/>
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card>
          <a-row>
            <a-col :span="12">
              <a-statistic
                title="预计今日阅读"
                :value="statistic.todayViewIncrease"
                :value-style="{ color: '#0000ff' }"
              >
                <template #suffix>
                  <UserOutlined/>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="12">
              <a-statistic
                title="预计今日阅读增长"
                :value="statistic.todayViewIncreaseRateAbs"
                :precision="2"
                suffix="%"
                class="demo-class"
                :value-style="statistic.todayViewIncreaseRate < 0 ? { color: '#3f8600' } : { color: '#cf1322' }"
              >
                <template #prefix>
                  <arrow-down-outlined v-if="statistic.todayViewIncreaseRate < 0"/>
                  <arrow-up-outlined v-if="statistic.todayViewIncreaseRate >= 0"/>
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts">
  import {defineComponent, onMounted, ref} from 'vue';
  import axios from 'axios';

  export default defineComponent({
    name: 'the-welcome',
    setup() {
      const statistic = ref()
      statistic.value = {}

      const getStatistic = () => {
        axios.get('/ebook-snapshot/get-statistic').then((resp) => {
          const data = resp.data
          if (data.success) {
            const statisticResp = data.content
            statistic.value.viewCount = statisticResp[1].viewCount
            statistic.value.voteCount = statisticResp[1].voteCount
            statistic.value.todayViewCount = statisticResp[1].viewIncrease
            statistic.value.todayVoteCount = statisticResp[1].voteIncrease

            //  按分钟计算当前时间点，占一天的百分比
            const now = new Date()
            const nowRate = (now.getHours() * 60 + now.getMinutes()) / (24 * 60)
            //  todayViewIncrease: 预计今日阅读
            //  todayViewIncreaseRate: 预计今日阅读率: (今天预计 - 昨天阅读数) - 昨天阅读数
            //  todayViewIncreaseRateAbs: 预计今日阅读增长
            statistic.value.todayViewIncrease = parseInt(String(statisticResp[1].viewIncrease / nowRate))
            statistic.value.todayViewIncreaseRate = (statistic.value.todayViewIncrease - statisticResp[0].viewIncrease)
              / statisticResp[0].viewIncrease * 100
            //  Abs: 绝对值
            statistic.value.todayViewIncreaseRateAbs = Math.abs(statistic.value.todayViewIncreaseRate)
          }
        })
      }

      onMounted(() => {
        getStatistic()
      })

      return {
        statistic
      }
    }
  });
</script>

<style>
</style>