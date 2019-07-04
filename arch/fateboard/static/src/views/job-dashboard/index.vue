<template>
  <div class="dashboard-container bg-dark app-container">
    <!--标题-->
    <h3 class="app-title flex space-between">
      <span>Dashboard</span>
      <p>Job: <span class="text-primary pointer" @click="toDetails(jobId)">{{ jobId }}</span></p>
    </h3>
    <!--上方三个模块-->
    <el-row :gutter="24" class="dash-board-list">
      <el-col :span="8">
        <div v-loading="datasetLoading" class="col dataset-info shadow">

          <!--左上角 job info 模块-->
          <h3 class="list-title">DATASET INFO</h3>
          <el-row :gutter="4" class="dataset-row">
            <el-col :span="6" :offset="2" class="dataset-title">GUEST</el-col>
            <el-col :span="8">
              <div class="dataset-item">
                <p class="name">dataset</p>
                <p class="value">{{ dataset.dataset }}</p>
              </div>
              <div class="dataset-item">
                <p class="name">columns</p>
                <p class="value">{{ dataset.columns }}</p>
              </div>
            </el-col>

            <el-col :span="8">
              <div class="dataset-item">
                <p class="name">target</p>
                <p class="value">{{ dataset.target }}</p>
              </div>
              <div class="dataset-item">
                <p class="name">rows</p>
                <p class="value">{{ dataset.row }}</p>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="4" class="dataset-row">
            <el-col :span="6" :offset="2" class="dataset-title">HOST</el-col>
            <el-col :span="8">
              <div class="dataset-item">
                <p class="name">partner</p>
                <p class="value">{{ dataset.partner }}</p>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="dataset-item">
                <p class="name">pnr-dataset</p>
                <p class="value">{{ dataset.pnr_dataset }}</p>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-col>

      <!--仪表盘job进度图-->
      <el-col :span="8">
        <div class="col job flex-center justify-center shadow">
          <h3 class="list-title">JOB</h3>
          <!--在faied或complete状态不展示仪表，展示数据，和进入details的按钮-->
          <div v-if="jobStatus==='faied' || jobStatus==='complete'" class="job-end-container flex flex-col flex-center">
            <i
              v-if="jobStatus === 'faied'"
              class="el-icon-circle-close job-icon icon-error"
              style="color: #ff6464;"/>
            <i
              v-else
              class="el-icon-circle-check job-icon icon-error"
              style="color: #24b68b;"/>
            <ul class="job-info flex space-around flex-wrap w-100">
              <li>
                <p class="name">status</p>
                <p class="value">{{ jobStatus }}</p>
              </li>
              <li>
                <p class="name">duration</p>
                <p class="value">00:30:35</p>
              </li>
            </ul>
            <el-button type="primary" round @click="toDetails(jobId)">VIEW THIS JOB</el-button>
          </div>
          <!--仪表盘图-->
          <div v-else class="echarts-container">
            <div v-if="elapsed" class="elapsed">
              <p class="elapsed-title">elapsed</p>
              <p class="elapsed-time text-primary">{{ elapsed }}</p>
            </div>
            <echart-container :class="'echarts'" :options="jobOptions" @getEchartInstance="getJobEchartInstance"/>
            <div class="bottom-wrapper w-100 flex flex-col flex-center">
              <span class="status">{{ jobStatus }}</span>
              <div style="width: 100%;" class="flex space-around">
                <el-button class="btn text-primary" round @click="killJob">KILL</el-button>
                <!--<el-button class="btn text-primary" round @click="completeJob">COMPLETE</el-button>-->
              </div>
            </div>
          </div>
        </div>
      </el-col>
      <!--graph图-->
      <el-col :span="8">
        <div v-loading="!DAGData" class="col graph flex-center justify-center shadow">
          <h3 class="list-title">GRAPH</h3>
          <div v-if="DAGData" :style="{'min-height':DAGData.componentList.length * 60+'px'}" class="wrapper w-100">
            <echart-container
              :class="'echarts'"
              :options="graphOptions"
              @getEchartInstance="getGraphEchartInstance"
            />
          </div>
        </div>
      </el-col>
    </el-row>

    <!--日志-->
    <div class="log-wrapper shadow">
      <h3 class="title">LOG</h3>
      <ul class="tab-bar flex">
        <li
          v-for="(tab,index) in Object.keys(logsMap)"
          :key="index"
          :class="{'tab-btn-active':currentLogTab === tab}"
          class="tab-btn"
          @click="switchLogTab(tab)"
        >
          <span class="text">{{ tab }}</span>
          <span v-if="tab!=='all'" :class="[tab]" class="count">{{ logsMap[tab].length }}</span>
        </li>
        <!--<div class="tab-search">debug</div>-->
      </ul>
      <div v-loading="logLoading" class="log-container" @mousewheel="logOnMousewheel">
        <ul class="log-list overflow-hidden">
          <li v-for="(log,index) in logsMap[currentLogTab].list" :key="index">
            <div class="flex">
              <span style="color:#999;margin-right: 5px;">{{ log.lineNum }}</span>
              <span> {{ log.content }}</span>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import EchartContainer from '@/components/EchartContainer'
import jobOptions from '@/utils/chart-options/gauge'
import graphOptions from '@/utils/chart-options/graph'
import graphChartHandle from '@/utils/vendor/graphChartHandle'
import { formatSeconds, initWebSocket } from '@/utils'

import { getJobDetails, getDAGDpencencies, queryLog } from '@/api/job'

export default {
  components: {
    EchartContainer
  },
  data() {
    return {
      jobOptions,
      graphOptions,
      dataset: {
        dataset: '',
        target: '',
        columns: '',
        row: '',
        partner: '',
        pnr_dataset: ''
      },
      jobStatus: 'waiting...',
      datasetLoading: true,
      logLoading: false,
      jobTimer: null,
      logWebsocket: null,
      jobWebsocket: null,
      logsMap: {
        'all': { list: [], length: 0 },
        'error': { list: [], length: 0 },
        'warning': { list: [], length: 0 },
        'info': { list: [], length: 0 },
        'debug': { list: [], length: 0 }
      },
      jobId: this.$route.query.jobId,
      DAGData: null,
      gaugeInstance: null,
      elapsed: '',
      currentLogTab: 'all'
    }
  },
  mounted() {
    // console.log(process.env.BASE_API)
    getJobDetails(this.jobId).then(res => {
      this.datasetLoading = false
      this.dataset = res.data.dataset
    })

    getDAGDpencencies(this.jobId).then(res => {
      this.DAGData = res.data
    })

    // this.logWebsocket = initWebSocket(`/log/${this.jobId}/default/default`, res => {
    //   // console.log('日志推送websocket连接成功')
    // }, res => {
    //   // console.log('websocket请求回来的数据:', res)
    //   this.allLogs.push(JSON.parse(res.data))
    //   // console.log(res.data)
    // })

    Object.keys(this.logsMap).forEach(item => {
      const type = item === 'all' ? 'default' : item
      this.logWebsocket = initWebSocket(`/log/${this.jobId}/default/${type}`, res => {
        // console.log('日志推送websocket连接成功')
      }, res => {
        // console.log('websocket请求回来的数据:', res)
        const data = JSON.parse(res.data)
        this.logsMap[item].list.push(data)
        if (item !== 'all') {
          this.logsMap[item].length = data.lineNum
        }
        // console.log(res.data)
      })
    })

    this.jobWebsocket = initWebSocket(`/websocket/progress/${this.jobId}`, res => {
      console.log('job推送websocket连接成功')
    }, res => {
      // console.log(res.data)
      if (this.jobStatus !== 'faied' && this.jobStatus !== 'complete') {
        const { process, status, time } = JSON.parse(res.data)
        if (status === 'running') {
          this.elapsed = formatSeconds(time)
          this.jobStatus = 'running...'
          this.jobOptions.series[0].pointer.show = true
          this.jobOptions.series[0].detail.show = true
          this.jobOptions.series[0].data[0].value = process
          this.gaugeInstance.setOption(this.jobOptions, true)
        }
      }
    })
  },
  beforeDestroy() {
    clearInterval(this.jobTimer)
    this.closeWebsocket()
  },
  methods: {
    // 获取job仪表图echart实例
    getJobEchartInstance(echartInstance) {
      this.gaugeInstance = echartInstance
    },

    // 关闭所有websocket
    closeWebsocket() {
      console.log('close Websocket')
      this.logWebsocket.close()
      this.jobWebsocket.close()
    },
    // stop job
    killJob() {
      console.log(this.jobWebsocket)
      this.$confirm('You can\'t undo this action', 'Are you sure you want to kill this job?', {
        confirmButtonText: 'Sure',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        // console.log('kill job:' + this.jobId)
        this.jobStatus = 'faied'
      }).catch(() => {
        console.log('cancel kill')
      })
    },
    completeJob() {
      this.jobStatus = 'complete'
    },
    // 获取graph树状图图echart实例
    getGraphEchartInstance(echartInstance) {
      let fnInterval = null
      const fn = () => {
        // 2. 如果有，则取消定时器，开始画图
        if (this.DAGData) {
          window.clearInterval(fnInterval)
          // 3. 根据自定义处理流程图中间件，获取流程节点和连接信息
          const { dataList, linksList } = graphChartHandle(this.DAGData)
          // console.log(dataList, linksList)
          this.graphOptions.series[0].data = dataList
          this.graphOptions.series[0].links = linksList
          echartInstance.setOption(this.graphOptions, true)
          // 4. 设置点击交互（暂无交互）
          echartInstance.on('click', { dataType: 'node' }, nodeData => {
            console.log(nodeData)
          })
        }
      }
      // 1. 开个定时器监听DAGData
      fnInterval = window.setInterval(fn, 100)
    },
    // 跳转到job详情
    toDetails(jobId) {
      this.$router.push({
        path: '/details',
        query: { jobId, 'from': 'Dashboard' }
      })
    },
    // 切换日志tab
    switchLogTab(tab) {
      this.currentLogTab = tab
    },
    // 鼠标滚轮上划加载前面日志
    logOnMousewheel(e) {
      // console.log(e.target.parentNode.parentNode.scrollTop)
      // console.log(e.wheelDelta)
      const topLog = this.logsMap[this.currentLogTab].list[0]
      if (!topLog) {
        return
      }
      const end = topLog.lineNum - 1
      if (end > 0) {
        if (e.target.parentNode.parentNode.scrollTop === 0 && (e.wheelDelta > 0 || e.detail > 0)) {
          // console.log('鼠标滚轮往上滑，加载前面的日志')
          const begin = end - 10 > 1 ? end - 10 : 1
          if (!this.logLoading) {
            this.logLoading = true

            const fn = () => {
              queryLog({
                componentId: 'default',
                jobId: this.jobId,
                begin,
                end
              }).then(res => {
                // console.log(res)
                const newLogs = []
                res.data.map(log => {
                  // console.log(log)
                  if (log) {
                    newLogs.push(log)
                  }
                })
                this.logsMap[this.currentLogTab].list = [...newLogs, ...this.logsMap[this.currentLogTab].list]
                this.logLoading = false
              }).catch(() => {
                this.logLoading = false
              })
            }

            window.setTimeout(fn, 1000)
          }
        }
      }
    }
  }
}
</script>

<style lang="scss">
  @import "../../styles/dashboard";
</style>
