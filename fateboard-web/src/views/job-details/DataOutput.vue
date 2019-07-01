<template>
  <section>
    <el-table
      :data="tBody"
      highlight-current-row
      border
      size="mini"
      height="450"
      style="width: 100%;"
    >
      <el-table-column type="index" label="index" width="134" align="center"/>
      <el-table-column
        v-for="(item,index) in header[page-1]"
        :key="index"
        :label="item.label"
        :prop="item.prop"
        width="134"
        align="center"/>
    </el-table>
    <div class="flex flex-end">
      <div v-if="total>0" class="pagination flex flex-center">
        <span>Total: {{ total }}</span>
        <i class="el-icon-arrow-left icon-arrow pointer" @click="changePage('minus')"/>
        <div v-if="total<=5" class="flex flex-center">
          <span
            v-for="(item,index) in totalArray"
            :key="index"
            :class="{'page-count-active':page===item}"
            class="page-count pointer"
            @click="page=item"
          >{{ item }}
          </span>
        </div>
        <div v-else class="flex flex-center">
          <span :class="{'page-count-active':page===1}" class="page-count pointer" @click="page=1">1</span>
          <span :class="{'page-count-active':page===2}" class="page-count pointer" @click="page=2">2</span>
          <span :class="{'page-count-active':page===3}" class="page-count pointer" @click="page=3">3</span>
          <span> ... </span>
          <span :class="{'page-count-active':page===total}" class="page-count pointer" @click="page=total">{{ total }}</span>
        </div>
        <i class="el-icon-arrow-right icon-arrow pointer" @click="changePage('plus')"/>
        <!--<div class="skip-wrapper">-->
        <!--<span>Skip To: </span>-->
        <!--</div>-->
      </div>
    </div>
    <!--<pagination/>-->
    <!--分页器-->
    <!--<pagination-->
    <!--v-show="total>0"-->
    <!--:total="total"-->
    <!--:page.sync="page"-->
    <!--:limit.sync="pageSize"-->
    <!--@pagination="getList"-->
    <!--/>-->
  </section>
</template>

<script>
import Pagination from '@/components/Pagination'

export default {
  name: 'DataOutput',
  components: {
    Pagination
  },
  props: {
    tHeader: {
      type: Array,
      default() {
        return []
      }
    },
    tBody: {
      type: Array,
      default() {
        return []
      }
    }
  },
  data() {
    return {
      page: 1,
      skip: '',
      pageSize: 10
    }
  },
  computed: {
    totalArray() {
      const arr = []
      for (let i = 1; i <= this.total; i++) {
        arr.push(i)
      }
      return arr
    },
    total() {
      return Math.ceil(this.tHeader.length / this.pageSize)
    },
    header() {
      return this.sliceArray(this.tHeader)
    }
  },
  mounted() {
  },
  methods: {
    sliceArray(arr) {
      let index = 0
      const newArr = []
      while (index < arr.length) {
        newArr.push(arr.slice(index, index += this.pageSize))
      }
      return newArr
    },
    changePage(op) {
      if (op === 'plus') {
        if (this.page < this.total) {
          ++this.page
        }
      } else if (op === 'minus') {
        if (this.page > 1) {
          --this.page
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .pagination {
    margin-top: 24px;
    font-size: 16px;
    color: #7f7d8e;
    .icon-arrow {
      width: 24px;
      height: 24px;
      margin: 0 6px;
      line-height: 24px;
      text-align: center;
    }
    .page-count {
      box-sizing: content-box;
      min-width: 14px;
      height: 24px;
      padding: 0 5px;
      margin: 0 6px;
      border-radius: 24px;
      line-height: 24px;
      text-align: center;
      &:hover {
        background: #494ece;
        color: #fff;
      }
    }
    .page-count-active {
      background: #494ece;
      color: #fff;
    }
    .skip-wrapper {
    }
  }
</style>
