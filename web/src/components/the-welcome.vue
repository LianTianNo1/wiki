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
    <br>
    <a-row>
      <a-col :span="24">
        <div id="main" style="width: 100%;height: 300px;"></div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts">
  import {defineComponent, onMounted, ref} from 'vue';
  import axios from 'axios';

  declare let echarts: any

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

      //------------------------ echarts图形展示 ------------------------
      const get30DayStatistic = () => {
        axios.get('/ebook-snapshot/get-30-statistic').then((response) => {
          const data = response.data
          if (data.success) {
            const statisticList = data.content

            init30DayEcharts(statisticList)
          }
        })
      }

      const init30DayEcharts = (list: any) => {
        // 基于准备好的dom，初始化echarts实例
        const myChart = echarts.init(document.getElementById('main'))

        //  横轴坐标
        const xAxis = []
        //  纵轴数据
        const seriesView = []
        const seriesVote = []
        for (let i = 0; i < list.length; i++) {
          const record = list[i]
          xAxis.push(record.date)
          seriesView.push(record.viewIncrease)
          seriesVote.push(record.voteIncrease)
        }

        // 指定图表的配置项和数据
        const option = {
          title: {
            text: '30天趋势图'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['总阅读量', '总点赞量']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxis
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '总阅读量',
              type: 'line',
              data: seriesView,
              smooth: true
            },
            {
              name: '总点赞量',
              type: 'line',
              data: seriesVote,
              smooth: true
            }
          ]
        }

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option)
      }

      onMounted(() => {
        getStatistic()
        get30DayStatistic()
      })

      return {
        statistic
      }
    }
  });
</script>

<style>
</style>