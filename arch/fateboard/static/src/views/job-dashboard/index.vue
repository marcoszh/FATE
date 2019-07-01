<template>
  <div class="dashboard-container bg-dark app-container">
    <h3 class="app-title flex space-between">
      <span>Dashboard</span>
      <p>Job: <span class="text-primary pointer" @click="toDetails(jobId)">{{ jobId }}</span></p>
    </h3>
    <el-row :gutter="24" class="dash-board-list">
      <el-col :span="8">
        <div v-loading="datasetLoading" class="col dataset-info shadow">
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
                <!--<el-button class="btn" @click="tWaitJob">WAITING</el-button>-->
                <!--<el-button class="btn" @click="tRunJob">RUNNING</el-button>-->
                <!--<el-button class="btn" @click="tCompJob">COMPLETE</el-button>-->
              </div>
            </div>
          </div>
        </div>
      </el-col>
      <!--graph图-->
      <el-col :span="8">
        <div v-loading="!DAGData" class="col graph flex-center justify-center shadow">
          <h3 class="list-title">GRAPH</h3>
          <div v-if="DAGData" :style="{height:DAGData.componentList.length * 60+'px'}" class="wrapper w-100">
            <echart-container
              :class="'echarts'"
              :options="graphOptions"
              @getEchartInstance="getGraphEchartInstance"
            />
          </div>
        </div>
      </el-col>
    </el-row>
    <div class="log-wrapper shadow">
      <h3 class="title">LOG</h3>
      <div class="tab-bar flex">
        <div :class="{'tab-btn-active':currentLogTab === 'all'}" class="tab-btn" @click="switchLogTab('all')">
          <span class="text">all</span>
        </div>
        <div :class="{'tab-btn-active':currentLogTab === 'error'}" class="tab-btn" @click="switchLogTab('error')">
          <span class="text">error</span>
          <span class="count error">5</span>
        </div>
        <div :class="{'tab-btn-active':currentLogTab === 'warning'}" class="tab-btn" @click="switchLogTab('warning')">
          <span class="text">warning</span>
          <span class="count warning">5</span>
        </div>
        <div :class="{'tab-btn-active':currentLogTab === 'info'}" class="tab-btn" @click="switchLogTab('info')">
          <span class="text">info</span>
          <span
            class="count info">5</span>
        </div>
        <div :class="{'tab-btn-active':currentLogTab === 'debug'}" class="tab-btn" @click="switchLogTab('debug')">
          <span class="text">debug</span>
        </div>
        <!--<div class="tab-search">debug</div>-->
      </div>
      <div v-loading="logLoading" class="log-container" @mousewheel="logOnMousewheel">
        <ul class="log-list">
          <li v-for="(log,index) in logs" :key="index">
            <span style="color:#999;margin-right: 5px;">{{ log.lineNum }}</span>
            {{ log.content }}
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
import { formatSeconds } from '@/utils'

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
      logs: [],
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

    // console.log(process.env.WEBSOCKET_BASE_API)
    this.logWebsocket = this.initWebSocket(`/log/default/${this.jobId}/1`, res => {
      // console.log('日志推送websocket连接成功')
    }, res => {
      // console.log('websocket请求回来的数据:', res)
      this.logs.push(JSON.parse(res.data))
      // console.log(res.data)
    })

    this.jobWebsocket = this.initWebSocket(`/websocket/progress/${this.jobId}`, res => {
      // console.log('job推送websocket连接成功')
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

    closeWebsocket() {
      console.log('close Websocket')
      this.logWebsocket.close()
      this.jobWebsocket.close()
    },
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
    tWaitJob() {
      clearInterval(this.jobTimer)
      this.jobStatus = 'waiting...'
      this.jobOptions.series[0].pointer.show = false
      this.jobOptions.series[0].detail.show = false
      this.gaugeInstance.setOption(this.jobOptions, true)
    },
    tRunJob() {
      this.jobStatus = 'running...'
      this.jobOptions.series[0].pointer.show = true
      this.jobOptions.series[0].detail.show = true
      this.jobTimer = setInterval(() => {
        this.jobOptions.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0
        this.gaugeInstance.setOption(this.jobOptions, true)
      }, 2000)
    },
    // 获取graph树状图图echart实例
    getGraphEchartInstance(echartInstance) {
      let fnInterval = null
      const fn = () => {
        if (this.DAGData) {
          window.clearInterval(fnInterval)
          const { dataList, linksList } = graphChartHandle(this.DAGData)
          this.graphOptions.series[0].data = dataList
          this.graphOptions.series[0].links = linksList
          echartInstance.setOption(this.graphOptions, true)
          // 点击交互
          echartInstance.on('click', { dataType: 'node' }, nodeData => {
            console.log(nodeData)
          })
        }
      }
      fnInterval = window.setInterval(fn, 100)
    },
    initWebSocket(url, onopen, onmessage, onclose = null) {
      // 创建一个websocket连接
      const instance = new WebSocket(process.env.WEBSOCKET_BASE_API + url)
      // websocket建立连接时会触发此方法
      instance.onopen = onopen
      // 客户端接收服务端数据时触发
      instance.onmessage = onmessage
      // 通信发生错误时触发
      instance.onerror = () => { // 如果请求出错则再次连接
        this.initWebSocket(url, instance)
      }
      instance.onclose = function() {
        // console.log('关闭websocket连接')
      }
      return instance
    },
    toDetails(jobId) {
      this.$router.push({
        path: '/details',
        query: { jobId, 'from': 'Dashboard' }
      })
    },
    switchLogTab(tab) {
      this.currentLogTab = tab
    },
    logOnMousewheel(e) {
      // console.log(e.target.parentNode.parentNode.scrollTop)
      // console.log(e.wheelDelta)
      const topLog = this.logs[0]
      if (!topLog) {
        return
      }
      const end = topLog.lineNum - 1
      if (end > 0) {
        if (e.target.parentNode.parentNode.scrollTop === 0 && (e.wheelDelta > 0 || e.detail > 0)) {
          // console.log('鼠标滚轮往上滑，加载前面的日志')
          this.logLoading = true
          const begin = end - 10 > 1 ? end - 10 : 1
          queryLog({
            componentId: 'default',
            jobId: this.jobId,
            begin,
            end
          }).then(res => {
            const newLogs = []
            res.data.map(log => {
              newLogs.push(JSON.parse(log))
            })
            console.log(newLogs)
            this.logs = [...newLogs, ...this.logs]
            this.logLoading = false
          })
        }
      }
    }
  }
}
</script>

<style lang="scss">
  @import "../../styles/dashboard";
</style>
