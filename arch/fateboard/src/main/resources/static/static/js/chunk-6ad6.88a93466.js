(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-6ad6"],{"+2Yw":function(t,e,n){(t.exports=n("I1BE")(!1)).push([t.i,".details-container {\n  height: 100%;\n}\n.details-container .top {\n    padding-bottom: 5px;\n    margin-bottom: 20px;\n    line-height: 25px;\n    border-bottom: 1px solid;\n}\n.details-container .top .link {\n      color: #409eff;\n}\n.details-container .section-wrapper {\n    /*max-height: 70vh;*/\n    margin-bottom: 32px;\n    overflow: auto;\n}\n.details-container .section-wrapper .section-title {\n      margin-bottom: 18px;\n      font-size: 18px;\n      color: #534c77;\n      font-weight: bold;\n}\n.details-container .section-wrapper .section-view {\n      padding: 20px 50px;\n      background: #fff;\n}\n.details-container .section-wrapper .section-view .tab-bar {\n        margin-bottom: 18px;\n}\n.details-container .section-wrapper .section-view .tab-bar .tab-btn {\n          display: -webkit-box;\n          display: -ms-flexbox;\n          display: flex;\n          -webkit-box-align: center;\n              -ms-flex-align: center;\n                  align-items: center;\n          margin-right: 24px;\n          padding: 0 5px;\n          background: #f8f8fa;\n          line-height: 26px;\n          border-radius: 26px;\n          cursor: pointer;\n}\n.details-container .section-wrapper .section-view .tab-bar .tab-btn .text {\n            padding: 0 10px;\n            font-size: 16px;\n            font-weight: bold;\n            color: #7f7d8e;\n}\n.details-container .section-wrapper .section-view .tab-bar .tab-btn:hover {\n            background: #494ece;\n}\n.details-container .section-wrapper .section-view .tab-bar .tab-btn:hover .text {\n              color: #fff;\n}\n.details-container .section-wrapper .section-view .tab-bar .tab-btn-active {\n          background: #494ece;\n}\n.details-container .section-wrapper .section-view .tab-bar .tab-btn-active .text {\n            color: #fff;\n}\n.details-container .section-wrapper .section-view .log-list > li {\n        /*height: 25px;*/\n        line-height: 25px;\n        text-indent: 10px;\n}\n.details-container .section-wrapper .section-view .echart {\n        width: calc(75vw - 20px);\n        /*width: 100%;*/\n        height: 500px;\n}\n.details-container .section-wrapper .section-view .tab {\n        max-height: 55vh;\n        overflow: auto;\n}\n.details-container .section-wrapper .job-summary {\n      min-height: 180px;\n      padding-top: 32px;\n      padding-left: 48px;\n}\n.details-container .section-wrapper .job-summary .section-col {\n        font-size: 16px;\n        display: -webkit-box;\n        display: -ms-flexbox;\n        display: flex;\n}\n.details-container .section-wrapper .job-summary .section-col .text-col {\n          margin-right: 28px;\n}\n.details-container .section-wrapper .job-summary .section-col .text-col .prop {\n            margin-bottom: 8px;\n            color: #bbbbc8;\n}\n.details-container .section-wrapper .job-summary .section-col .text-col .value {\n            margin-bottom: 8px;\n            font-weight: bold;\n            color: #7f7d8e;\n            overflow: hidden;\n            text-overflow: ellipsis;\n            white-space: nowrap;\n}\n.details-container .section-wrapper .output-wrapper {\n      height: 400px;\n      width: 50%;\n      padding: 0 20px;\n}\n.details-container .section-wrapper .output-wrapper:nth-child(1) {\n        border-right: 1px solid #999;\n}\n.details-container .section-wrapper .output-wrapper .echarts {\n        height: 100%;\n}\n.details-container .section-wrapper .output-wrapper .msg .msg-title {\n        margin-right: 20px;\n}\n",""])},"+A8E":function(t,e,n){var a=n("eAVi");"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);(0,n("SZ7m").default)("5dcdc66d",a,!0,{})},"/f1G":function(t,e,n){t.exports={default:n("nhzr"),__esModule:!0}},"0AMJ":function(t,e,n){"use strict";var a=n("+A8E");n.n(a).a},"7Qib":function(t,e,n){"use strict";n.d(e,"d",function(){return c}),n.d(e,"b",function(){return u}),n.d(e,"c",function(){return p}),n.d(e,"a",function(){return d});n("jWXv"),n("rfXi"),n("gDS+");var a=n("P2sY"),i=n.n(a),o=n("GQeE"),r=n.n(o),s=n("EJiy"),l=n.n(s);function c(t,e){if(0===arguments.length)return null;var n=e||"{y}-{m}-{d} {h}:{i}:{s}",a=void 0;"object"===(void 0===t?"undefined":l()(t))?a=t:("string"==typeof t&&/^[0-9]+$/.test(t)&&(t=parseInt(t)),"number"==typeof t&&10===t.toString().length&&(t*=1e3),a=new Date(t));var i={y:a.getFullYear(),m:a.getMonth()+1,d:a.getDate(),h:a.getHours(),i:a.getMinutes(),s:a.getSeconds(),a:a.getDay()};return n.replace(/{(y|m|d|h|i|s|a)+}/g,function(t,e){var n=i[e];return"a"===e?["日","一","二","三","四","五","六"][n]:(t.length>0&&n<10&&(n="0"+n),n||0)})}function u(t){var e=Math.floor(t/3600),n=Math.floor(t/60%60),a=Math.floor(t%60),i=function(t){return t<1?"00":t<10?"0"+t:t.toString()};return(e=i(e))+":"+(n=i(n))+":"+(a=i(a))}function p(t,e){var n=[{prop:"name",label:e}],a=!0,o=[];for(var s in t){var l=t[s];if(a)r()(l).forEach(function(t){n.push({prop:t,label:t})}),a=!1;var c=i()({name:s},l);o.push(c)}return{header:n,data:o}}function d(t){if(!t&&"object"!==(void 0===t?"undefined":l()(t)))throw new Error("error arguments","deepClone");var e=t.constructor===Array?[]:{};return r()(t).forEach(function(n){t[n]&&"object"===l()(t[n])?e[n]=d(t[n]):e[n]=t[n]}),e}},"7z+L":function(t,e,n){var a=n("gYtT");"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);(0,n("SZ7m").default)("12b0fd0e",a,!0,{})},BSnc:function(t,e,n){var a=n("+2Yw");"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);(0,n("SZ7m").default)("86c5fd5e",a,!0,{})},Bw9L:function(t,e,n){"use strict";var a=n("BSnc");n.n(a).a},DfOl:function(t,e,n){"use strict";var a=n("a+p0");n.n(a).a},E8gZ:function(t,e,n){var a=n("w6GO"),i=n("NsO/"),o=n("NV0k").f;t.exports=function(t){return function(e){for(var n,r=i(e),s=a(r),l=s.length,c=0,u=[];l>c;)o.call(r,n=s[c++])&&u.push(t?[n,r[n]]:r[n]);return u}}},HMEu:function(t,e,n){(t.exports=n("I1BE")(!1)).push([t.i,".pagination[data-v-55adaf56] {\n  margin-top: 24px;\n  font-size: 16px;\n  color: #7f7d8e;\n}\n.pagination .icon-arrow[data-v-55adaf56] {\n    width: 24px;\n    height: 24px;\n    margin: 0 6px;\n    line-height: 24px;\n    text-align: center;\n}\n.pagination .page-count[data-v-55adaf56] {\n    -webkit-box-sizing: content-box;\n            box-sizing: content-box;\n    min-width: 14px;\n    height: 24px;\n    padding: 0 5px;\n    margin: 0 6px;\n    border-radius: 24px;\n    line-height: 24px;\n    text-align: center;\n}\n.pagination .page-count[data-v-55adaf56]:hover {\n      background: #494ece;\n      color: #fff;\n}\n.pagination .page-count-active[data-v-55adaf56] {\n    background: #494ece;\n    color: #fff;\n}\n",""])},JTod:function(t,e,n){"use strict";var a=n("mq1e");n.n(a).a},Mz3J:function(t,e,n){"use strict";Math.easeInOutQuad=function(t,e,n,a){return(t/=a/2)<1?n/2*t*t+e:-n/2*(--t*(t-2)-1)+e};var a=window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame||function(t){window.setTimeout(t,1e3/60)};function i(t,e,n){var i=document.documentElement.scrollTop||document.body.parentNode.scrollTop||document.body.scrollTop,o=t-i,r=0;e=void 0===e?500:e;!function t(){r+=20,function(t){document.documentElement.scrollTop=t,document.body.parentNode.scrollTop=t,document.body.scrollTop=t}(Math.easeInOutQuad(r,i,o,e)),r<e?a(t):n&&"function"==typeof n&&n()}()}var o={name:"Pagination",props:{total:{required:!0,type:Number},page:{type:Number,default:1},limit:{type:Number,default:20},pageSizes:{type:Array,default:function(){return[10,20,30,50]}},layout:{type:String,default:"total, sizes, prev, pager, next, jumper"},background:{type:Boolean,default:!0},autoScroll:{type:Boolean,default:!0},hidden:{type:Boolean,default:!1}},computed:{currentPage:{get:function(){return this.page},set:function(t){this.$emit("update:page",t)}},pageSize:{get:function(){return this.limit},set:function(t){this.$emit("update:limit",t)}}},methods:{handleSizeChange:function(t){this.$emit("pagination",{page:this.currentPage,limit:t}),this.autoScroll&&i(0,800)},handleCurrentChange:function(t){this.$emit("pagination",{page:t,limit:this.pageSize}),this.autoScroll&&i(0,800)}}},r=(n("0AMJ"),n("KHd+")),s=Object(r.a)(o,function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"pagination-container flex flex-end",class:{hidden:t.hidden}},[n("el-pagination",t._b({attrs:{background:t.background,"current-page":t.currentPage,"page-size":t.pageSize,layout:t.layout,"page-sizes":t.pageSizes,total:t.total},on:{"update:currentPage":function(e){t.currentPage=e},"update:pageSize":function(e){t.pageSize=e},"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}},"el-pagination",t.$attrs,!1))],1)},[],!1,null,"006d6ecc",null);s.options.__file="index.vue";e.a=s.exports},TJnn:function(t,e,n){"use strict";var a=n("7z+L");n.n(a).a},Yulh:function(t,e,n){"use strict";var a=n("MT78"),i=n.n(a),o={props:{className:{type:String,default:""},id:{type:String,default:""},options:{type:Object,default:function(){return{}}}},data:function(){return{echarts:i.a,echartInstance:null}},mounted:function(){this.initChart()},beforeDestroy:function(){this.echartInstance&&(this.echartInstance.dispose(),this.echartInstance=null,window.removeEventListener("resize",this.resize))},methods:{initChart:function(){this.echartInstance=this.echarts.init(this.$refs.myEchart),window.addEventListener("resize",this.resize),this.$emit("getEchartInstance",this.echartInstance),this.$emit("getEchart",this.echarts),this.echartInstance.setOption(this.options)},resize:function(){this.echartInstance.resize()}}},r=(n("TJnn"),n("KHd+")),s=Object(r.a)(o,function(){var t=this.$createElement;return(this._self._c||t)("div",{ref:"myEchart",class:this.className,attrs:{id:this.id}})},[],!1,null,null,null);s.options.__file="index.vue";e.a=s.exports},"a+p0":function(t,e,n){var a=n("HMEu");"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);(0,n("SZ7m").default)("bfc0eaaa",a,!0,{})},bZCS:function(t,e,n){"use strict";e.a=function(t){var e=t.dependencies,n=t.componentList,a=0,i=0,o=[],r=[],s=[],l=[];n.forEach(function(t){l.push(t.componentName)});for(var c=function(t){var c=e[n[t].componentName],u="#333";if("faied"===n[t].status?u="#ff6464":"running"===n[t].status?u="#494ece":"success"===n[t].status&&(u="#24b68b"),c){for(var p=0;p<c.length;p++)s.push({target:t,source:l.indexOf(c[p])});var d=null;r.forEach(function(t){for(var e=0;e<c.length;e++)t.name===c[e]&&(d?t.level<d.level&&(d=t):d=t)}),d&&a<=d.level?(++a,o.push(i),i=1):++i,t===n.length-1&&o.push(i)}else++i;r.push({name:n[t].componentName,level:a,index:i,label:{color:u,borderColor:u}})},u=0;u<n.length;u++)c(u);var p=Math.max.apply(Math,o),d=10*(p-1),h=0;return r.map(function(t,e){var n=o[t.level],a=0;a=n===p?10*(t.index-1)*7:d/(n+1)*t.index*7,t.x=a;for(var i=e;i>=0;i--)if(r[i].level===t.level){++h;break}t.y=10*(t.level+h)}),{dataList:r,linksList:s}}},dv4G:function(t,e,n){"use strict";n.d(e,"a",function(){return i}),n.d(e,"b",function(){return o}),n.d(e,"f",function(){return r}),n.d(e,"e",function(){return s}),n.d(e,"d",function(){return l}),n.d(e,"c",function(){return c}),n.d(e,"g",function(){return u});var a=n("t3Un");function i(t){return Object(a.a)({url:"/job/query/all",method:"get",params:t})}function o(t){return Object(a.a)({url:"/job/query/status",method:"get",params:t})}function r(t){return Object(a.a)({url:"/job/v1/pipeline/job/stop",method:"post",data:{job_id:t}})}function s(t){return Object(a.a)({url:"/job/query/"+t,method:"get"})}function l(t){return Object(a.a)({url:"/v1/pipeline/dag/dependencies",method:"post",data:{job_id:t}})}function c(t){return Object(a.a)({url:"/v1/tracking/component/parameters",method:"post",data:t})}function u(t){var e=t.componentId,n=t.jobId,i=t.begin,o=t.end;return Object(a.a)({url:"/queryLogWithSize/"+e+"/"+n+"/"+i+"/"+o+"  ",method:"get"})}},eAVi:function(t,e,n){(t.exports=n("I1BE")(!1)).push([t.i,"\n.pagination-container[data-v-006d6ecc] {\n  background: #fff;\n  padding: 32px 16px;\n}\n.pagination-container.hidden[data-v-006d6ecc] {\n  display: none;\n}\n",""])},fW1p:function(t,e,n){var a=n("Y7ZC"),i=n("E8gZ")(!1);a(a.S,"Object",{values:function(t){return i(t)}})},gYtT:function(t,e,n){(t.exports=n("I1BE")(!1)).push([t.i,"\n.default-echart {\n  width: 75vw;\n  height: 75vh;\n}\n",""])},lAiS:function(t,e,n){"use strict";e.a={tooltip:{},series:[{type:"graph",layout:"none",roam:!1,label:{show:!0,color:"#333",borderWidth:1,borderRadius:2,borderColor:"#333",padding:5,lineHeight:20},symbol:"roundRect",symbolSize:[60,20],symbolOffset:[0,0],edgeSymbol:["circle","arrow"],edgeSymbolSize:[6,10],data:[],links:[],itemStyle:{color:"transparent"},lineStyle:{normal:{opacity:.9,width:1,curveness:0}}}]}},mq1e:function(t,e,n){var a=n("rx6N");"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);(0,n("SZ7m").default)("604e92c2",a,!0,{})},nhzr:function(t,e,n){n("fW1p"),t.exports=n("WEpk").Object.values},"nr+k":function(t,e,n){"use strict";n.r(e);var a=n("P2sY"),i=n.n(a),o=n("gDS+"),r=n.n(o),s=n("/f1G"),l=n.n(s),c=n("GQeE"),u=n.n(c),p=n("7Qib"),d=n("dv4G"),h=n("t3Un");var m=n("Yulh"),v=n("bZCS"),g={name:"ModelOutput",components:{EchartContainer:m.a},props:{metricOutputList:{type:Array,default:function(){return[]}},modelOutputType:{type:String,default:""},modelOutput:{type:Object,default:function(){return{}}}},data:function(){return{binningSelectValue:"",stackBarInstance:null,woeInstance:null}},computed:{},mounted:function(){},updated:function(){"HeteroFeatureBinning"===this.modelOutputType&&this.modelOutput.options&&!this.binningSelectValue&&(this.binningSelectValue=this.modelOutput.options[0].value,this.stackBarInstance.setOption(this.modelOutput.stackBarData[this.binningSelectValue],!0),this.woeInstance.setOption(this.modelOutput.woeData[this.binningSelectValue],!0)),"HeteroFeatureBinning"===this.modelOutputType&&(this.stackBarInstance.setOption(this.modelOutput.stackBarData[this.binningSelectValue],!0),this.woeInstance.setOption(this.modelOutput.woeData[this.binningSelectValue],!0))},methods:{changebinning:function(t){this.stackBarInstance.setOption(this.modelOutput.stackBarData[t],!0),this.stackBarInstance.setOption(this.modelOutput.woeData[t],!0)},getStackBarInstance:function(t){this.stackBarInstance=t},getWoeInstance:function(t){this.woeInstance=t}}},b=(n("JTod"),n("KHd+")),f=Object(b.a)(g,function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("section",[n("ul",t._l(t.metricOutputList,function(e,a){return n("li",{key:a},["line"===e.type?n("div",[n("echart-container",{class:"echart",attrs:{options:e.data}})],1):t._e()])})),t._v(" "),t.modelOutputType&&t.modelOutput?n("div",["HeteroSecureBoost"===t.modelOutputType?n("div",[n("pre",[t._v(" "+t._s(t.modelOutput.formatString)+" ")])]):"DataIO"===t.modelOutputType?n("div",[n("el-table",{staticStyle:{"margin-bottom":"50px"},attrs:{data:t.modelOutput.imputerData,"highlight-current-row":"",fit:"",border:""}},[n("el-table-column",{attrs:{type:"index",label:"index",width:"100px",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"variable",prop:"variable",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"missing_fill_method",prop:"method",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"fill_value",prop:"value",align:"center"}})],1),t._v(" "),n("el-table",{attrs:{data:t.modelOutput.outlierData,"highlight-current-row":"",fit:"",border:""}},[n("el-table-column",{attrs:{type:"index",label:"index",width:"100px",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"variable",prop:"variable",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"outlier_fill_method",prop:"method",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"fill_value",prop:"value",align:"center"}})],1)],1):"FeatureScale"===t.modelOutputType?n("div",[n("p",[t._v("method:"+t._s(t.modelOutput.method))]),t._v(" "),n("el-table",{attrs:{data:t.modelOutput.tData,"highlight-current-row":"",fit:"",border:""}},[n("el-table-column",{attrs:{type:"index",label:"index",width:"100px",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"variable",prop:"variable",align:"center"}}),t._v(" "),t._l(t.modelOutput.tHeader,function(t,e){return n("el-table-column",{key:e,attrs:{label:t.label,prop:t.prop,align:"center"}})})],2)],1):"HeteroLR"===t.modelOutputType?n("div",[n("el-table",{attrs:{data:t.modelOutput.tData,"highlight-current-row":"",fit:"",border:""}},[n("el-table-column",{attrs:{type:"index",label:"index",width:"100px",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"variable",prop:"variable",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"weight",prop:"weight",align:"center"}})],1)],1):"HeteroFeatureSelection"===t.modelOutputType?n("div",[n("ul",t._l(t.modelOutput.chartData,function(t,e){return n("li",{key:e},[n("echart-container",{class:"echart",attrs:{options:t}})],1)}))]):"Intersection"===t.modelOutputType?n("div",[n("p",[t._v("intersection cout: "+t._s(t.modelOutput.intersection_cout))]),t._v(" "),n("p",[t._v("intersection ratio: "+t._s(t.modelOutput.intersection_ratio))])]):"HeteroFeatureBinning"===t.modelOutputType?n("div",[n("el-table",{attrs:{data:t.modelOutput.sourceData,"highlight-current-row":"",fit:"",border:""}},[n("el-table-column",{attrs:{type:"index",label:"index",width:"100px",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"variable",prop:"variable",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"IV",prop:"iv",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"woe",prop:"woe",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"monotonicity",prop:"monotonicity",align:"center"}})],1),t._v(" "),n("div",{staticStyle:{border:"1px solid #eee",padding:"30px","margin-top":"25px"}},[n("el-select",{on:{change:t.changebinning},model:{value:t.binningSelectValue,callback:function(e){t.binningSelectValue=e},expression:"binningSelectValue"}},t._l(t.modelOutput.options,function(t,e){return n("el-option",{key:e,attrs:{label:t.label,value:t.value}})})),t._v(" "),t.binningSelectValue?n("el-table",{staticStyle:{"margin-top":"20px"},attrs:{data:t.modelOutput.variableData[t.binningSelectValue],"highlight-current-row":"",fit:"",border:""}},[n("el-table-column",{attrs:{type:"index",label:"index",width:"100px",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"binning",prop:"binning",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"iv",prop:"iv",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"woe",prop:"woe",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"event_count",prop:"event_count",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"event_ratio",prop:"event_ratio",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"non_event_count",prop:"non_event_count",align:"center"}}),t._v(" "),n("el-table-column",{attrs:{label:"non_event_ratio",prop:"non_event_ratio",align:"center"}})],1):t._e()],1),t._v(" "),n("echart-container",{class:"echart",on:{getEchartInstance:t.getStackBarInstance}}),t._v(" "),n("echart-container",{class:"echart",on:{getEchartInstance:t.getWoeInstance}})],1):"selection"===t.modelOutputType?n("div",[t._v("\n      111\n    ")]):"sample"===t.modelOutputType?n("div",[t._v("\n      111\n    ")]):"hot"===t.modelOutputType?n("div",[t._v("\n      111\n    ")]):"evaluation"===t.modelOutputType?n("div",[t._v("\n      111\n    ")]):t._e()]):t._e()])},[],!1,null,"e3d8b862",null);f.options.__file="ModelOutput.vue";var _=f.exports,y={name:"DataOutput",components:{Pagination:n("Mz3J").a},props:{tHeader:{type:Array,default:function(){return[]}},tBody:{type:Array,default:function(){return[]}}},data:function(){return{page:1,skip:"",pageSize:10}},computed:{totalArray:function(){for(var t=[],e=1;e<=this.total;e++)t.push(e);return t},total:function(){return Math.ceil(this.tHeader.length/this.pageSize)},header:function(){return this.sliceArray(this.tHeader)}},mounted:function(){},methods:{sliceArray:function(t){for(var e=0,n=[];e<t.length;)n.push(t.slice(e,e+=this.pageSize));return n},changePage:function(t){"plus"===t?this.page<this.total&&++this.page:"minus"===t&&this.page>1&&--this.page}}},x=(n("DfOl"),Object(b.a)(y,function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("section",[n("el-table",{staticStyle:{width:"100%"},attrs:{data:t.tBody,"highlight-current-row":"",border:"",size:"mini",height:"450"}},[n("el-table-column",{attrs:{type:"index",label:"index",width:"134",align:"center"}}),t._v(" "),t._l(t.header[t.page-1],function(t,e){return n("el-table-column",{key:e,attrs:{label:t.label,prop:t.prop,width:"134",align:"center"}})})],2),t._v(" "),n("div",{staticClass:"flex flex-end"},[t.total>0?n("div",{staticClass:"pagination flex flex-center"},[n("span",[t._v("Total: "+t._s(t.total))]),t._v(" "),n("i",{staticClass:"el-icon-arrow-left icon-arrow pointer",on:{click:function(e){t.changePage("minus")}}}),t._v(" "),t.total<=5?n("div",{staticClass:"flex flex-center"},t._l(t.totalArray,function(e,a){return n("span",{key:a,staticClass:"page-count pointer",class:{"page-count-active":t.page===e},on:{click:function(n){t.page=e}}},[t._v(t._s(e)+"\n        ")])})):n("div",{staticClass:"flex flex-center"},[n("span",{staticClass:"page-count pointer",class:{"page-count-active":1===t.page},on:{click:function(e){t.page=1}}},[t._v("1")]),t._v(" "),n("span",{staticClass:"page-count pointer",class:{"page-count-active":2===t.page},on:{click:function(e){t.page=2}}},[t._v("2")]),t._v(" "),n("span",{staticClass:"page-count pointer",class:{"page-count-active":3===t.page},on:{click:function(e){t.page=3}}},[t._v("3")]),t._v(" "),n("span",[t._v(" ... ")]),t._v(" "),n("span",{staticClass:"page-count pointer",class:{"page-count-active":t.page===t.total},on:{click:function(e){t.page=t.total}}},[t._v(t._s(t.total))])]),t._v(" "),n("i",{staticClass:"el-icon-arrow-right icon-arrow pointer",on:{click:function(e){t.changePage("plus")}}})]):t._e()])],1)},[],!1,null,"55adaf56",null));x.options.__file="DataOutput.vue";var w=x.exports,O=n("lAiS"),S={title:{text:""},color:["#8e91df","#78d0b7"],tooltip:{trigger:"axis",axisPointer:{type:"shadow"}},legend:{show:!0,right:0,top:"3%",orient:"horizontal",itemWidth:15,itemHeight:15},grid:{left:"3%",right:"4%",bottom:"3%",containLabel:!0},yAxis:{type:"value"},xAxis:{type:"category",data:[]},series:[]},C={tooltip:{trigger:"item",triggerOn:"mousemove"},series:[{type:"tree",data:[{name:"test",children:[{name:"child1",children:[{name:"child1-1"},{name:"child1-2"}]},{name:"child2",children:[{name:"child2-1"},{name:"child2-2"},{name:"child2-3"}]}]}],left:"2%",right:"2%",top:"8%",bottom:"20%",symbol:"emptyCircle",symbolSize:[100,25],orient:"vertical",expandAndCollapse:!0,label:{normal:{position:"inside",verticalAlign:"middle",fontSize:12}},itemStyle:{},leaves:{label:{}},animationDurationUpdate:750}]},k={color:["#3398DB","#D22123","#20D252","#1022F0","#A21155"],tooltip:{trigger:"axis",axisPointer:{type:"shadow"}},grid:{left:"3%",right:"10%",bottom:"3%",containLabel:!0},xAxis:{type:"category",data:[],axisTick:{alignWithLabel:!0},name:"",nameLocation:"end",nameCap:10},yAxis:[{type:"value"}],series:[{name:"value",type:"line",smooth:!0,symbol:"none",data:[]}]},j={title:{text:""},tooltip:{trigger:"axis",axisPointer:{type:"shadow"}},grid:{left:"3%",right:"4%",bottom:"3%",containLabel:!0},yAxis:{type:"category",data:[],axisTick:{show:!1},axisLine:{show:!1},splitLine:{show:!1}},xAxis:{type:"value",axisTick:{show:!1},axisLine:{show:!1},axisLabel:{show:!1},splitLine:{show:!1}},series:[{type:"bar",barWidth:"20%",barGap:"-100%",itemStyle:{color:"#aaa"},data:[],label:{show:!0,position:"right",formatter:null},tooltip:{show:!1}},{name:"value",type:"bar",barWidth:"20%",data:[]}]},T={name:"JobDtails",components:{EchartContainer:m.a,ModelOutput:_,DataOutput:w},data:function(){return{jobId:this.$route.query.jobId,jobFrom:this.$route.query.from,status:"complete",summaryLoading:!0,msgLoading:!1,paraList:[],guest:{},jobInfo:{},componentName:"",graphOptions:O.a,treeOptions:C,lineOptions:k,doubleBarOptions:j,outputGraphOptions:O.a,paraLoading:!1,DAGData:null,modelSummaryData:null,outputVisible:!1,metricOutputList:[],modelOutputLoading:!1,modelOutputType:"",modelOutput:null,dataOutputHeader:[],dataOutputBody:[],outputTitle:"",currentTab:"model",logList:[],logWebsocket:null}},mounted:function(){var t=this;this.getDatasetInfo(),Object(d.d)(this.jobId).then(function(e){t.DAGData=e.data})},beforeDestroy:function(){this.closeWebsocket()},methods:{getDatasetInfo:function(){var t=this;Object(d.e)(this.jobId).then(function(e){t.summaryLoading=!1;var n=e.data,a=n.job,i=n.dataset;t.guest={dataset:i.dataset,target:i.target,rows:i.row,columns:i.columns,partner:i.partner,pnr_dataset:i.pnr_dataset},t.jobInfo={submmissionTime:Object(p.d)(new Date(a.fCreateTime)),startTime:Object(p.d)(new Date(a.fStartTime)),endTime:Object(p.d)(new Date(a.fEndTime)),duration:Object(p.b)(3800)},t.modelSummaryData=Object(p.c)(i.model_summary,"")})},initWebSocket:function(t,e,n){var a=this,i=(arguments.length>3&&void 0!==arguments[3]&&arguments[3],new WebSocket("ws://172.16.153.113:16688"+t));return i.onopen=e,i.onmessage=n,i.onerror=function(){a.initWebSocket(t,i)},i.onclose=function(){},i},toPrevPage:function(){console.log(this.$route);var t=null;"Job overview"===this.jobFrom?t="/history":"Dashboard"===this.jobFrom&&(t="/dashboard"),this.$router.push({path:t,query:{jobId:this.jobId}})},getGraphEchartInstance:function(t){var e=this,n=null;n=window.setInterval(function(){if(e.DAGData){window.clearInterval(n);var a=Object(v.a)(e.DAGData),i=a.dataList,o=a.linksList;e.graphOptions.series[0].data=i,e.graphOptions.series[0].links=o,t.setOption(e.graphOptions,!0),t.on("click",{dataType:"node"},function(t){e.clickComponent(t.name)})}},100)},clickComponent:function(t){this.initOutput(),this.componentName=t,this.paraLoading=!0,this.getParams(t),this.getMetrics(t),this.getModelOutput(t),this.outputVisible=!0,this.outputTitle=t},initOutput:function(){this.metricOutputList=[],this.modelOutput=null,this.modelOutputType="",this.dataOutputHeader=[],this.dataOutputBody=[],this.currentTab="model",this.logList=[],this.componentName="",this.closeWebsocket()},switchLogTab:function(t){var e=this;this.currentTab=t,"data"===t&&0===this.dataOutputHeader.length&&this.getDataOutput(this.componentName),"log"!==t||this.logWebsocket||(this.logWebsocket=this.initWebSocket("/log/"+this.componentName+"/"+this.jobId+"/1",function(t){},function(t){e.logList.push(JSON.parse(t.data))}))},closeWebsocket:function(){console.log("close Websocket"),this.logWebsocket&&(this.logWebsocket.close(),this.logWebsocket=null)},getParams:function(t){var e=this;Object(d.c)({job_id:this.jobId,component_name:t}).then(function(t){e.paraLoading=!1,e.paraList=u()(t.data)})},getMetrics:function(t){var e=this;(function(t){return Object(h.a)({url:"/v1/tracking/component/metrics",method:"post",data:t})})({job_id:this.jobId,component_name:t}).then(function(n){var a=n.data;a&&u()(a).forEach(function(n){a[n].forEach(function(a){(function(t){return Object(h.a)({url:"/v1/tracking/component/metric_data",method:"post",data:t})})({job_id:e.jobId,component_name:t,metric_namespace:n,metric_name:a}).then(function(t){e.modelOutputLoading=!1;var a=t.data,i=a.data,o=a.meta;if(i&&o){var r=o.metric_type,s=o.unit_name,c="";if("LOSS"===r){c="line";var d=Object(p.a)(k);d.xAxis.data=u()(i),d.xAxis.name=s,d.series[0].data=l()(i),e.metricOutputList.push({type:c,nameSpace:n,data:d})}"KS"===r&&(0===e.metricOutputList.length?e.metricOutputList.push({type:"KS",nameSpace:n,data:[1]}):e.metricOutputList.forEach(function(t,a){"KS"===t.type&&t.nameSpace===n?(console.log(t),e.metricOutputList[a].data.push(2)):e.metricOutputList.push({type:"KS",nameSpace:n,data:[1]})}))}})})})})},getDataOutput:function(t){var e=this;(function(t){return Object(h.a)({url:"/v1/tracking/component/output/data",method:"post",data:t})})({job_id:this.jobId,component_name:t}).then(function(t){console.log(t);var n=[],a=[];t.data.meta.header.forEach(function(t){n.push({prop:t,label:t})}),t.data.data.forEach(function(e){var n={};t.data.meta.header.forEach(function(t,a){n[t]=e[a]}),a.push(n)}),e.dataOutputHeader=n,e.dataOutputBody=a})},getModelOutput:function(t){var e=this;(function(t){return Object(h.a)({url:"/v1/tracking/component/output/model",method:"post",data:t})})({job_id:this.jobId,component_name:t}).then(function(n){e.modelOutputLoading=!1,e.modelOutputType=n.data.meta&&n.data.meta?n.data.meta.module_name:"";var a=n.data.data?n.data.data:"";if("HeteroSecureBoost"===e.modelOutputType)e.modelOutput={formatObj:a,formatString:r()(a,null,2)};else if("DataIO"===e.modelOutputType){var o=[],s=[],c=a.imputer_param,d=a.outlier_param;u()(c.missingReplaceValue).forEach(function(t){o.push({variable:t,method:c.strategy,value:c.missingReplaceValue[t]})}),u()(d.outlierReplaceValue).forEach(function(t){s.push({variable:t,method:d.strategy,value:d.outlierReplaceValue[t]})}),e.modelOutput={imputerData:o,outlierData:s}}else if("Intersection"===e.modelOutputType)e.modelOutput=a;else if("FeatureScale"===e.modelOutputType){var h=[],m=[],v=null,g=a.method;"min_max_scale"===g?(v=a.min_max_scale_param,h=[{prop:"min",label:"min"},{prop:"max",label:"max"},{prop:"range_min",label:"range_min"},{prop:"range_max",label:"range_max"}]):(v=a.standardScaleParam,g="standard_scale",h=[{prop:"mean",label:"mean"},{prop:"scale",label:"std"}]),v&&(u()(v).forEach(function(t){var e={variable:t};h.forEach(function(n,a){Array.isArray(v[t])?e[n.prop]=v[t][a]:e=i()(e,v[t])}),m.push(e)}),e.modelOutput={method:g,tHeader:h,tData:m})}else if("HeteroLR"===e.modelOutputType){var b=a.weight,f=a.intercept,_=a.converged,y=[];u()(b).forEach(function(t){y.push({variable:t,weight:b[t]})}),y.push({variable:"intercept",weight:f}),e.modelOutput={tData:y,converged:_}}else if("HeteroFeatureSelection"===e.modelOutputType){var x=[];a.results.forEach(function(t){var e=t.filterName,n=t.featureValues;if(e&&n){var a=Object(p.a)(j);a.title.text=e,a.yAxis.data=u()(n);var i=l()(n);a.series[0].label.formatter=function(t){return i[t.dataIndex]};var o=0;i.forEach(function(t){t>o&&(o=t)}),u()(n).forEach(function(){a.series[0].data.push(1.2*o)}),a.series[1].data=i,x.push(a)}}),e.modelOutput={chartData:x}}else if("HeteroFeatureBinning"===e.modelOutputType){var w=[],O=[],C={},k={},T={},I=a.binningResult.binningResult;u()(I).forEach(function(t){var e=[],n=0;I[t].ivArray.forEach(function(a,i,o){var r=I[t].splitPoints[i]||I[t].splitPoints[i-1],s="";s=0===n?"a < "+r:i===o.length-1?"a >= "+r:n+" <= a < "+r,n=r,e.push({binning:s,event_count:I[t].eventCountArray[i],event_ratio:I[t].eventRateArray[i],non_event_count:I[t].nonEventCountArray[i],non_event_ratio:I[t].nonEventRateArray[i],woe:I[t].woeArray[i],iv:I[t].ivArray[i]})}),C[t]=e;var a=Object(p.a)(S),i=Object(p.a)(S);a.series.push({name:"event count",type:"bar",data:I[t].eventCountArray,stack:"event"}),a.series.push({name:"non-event count",type:"bar",data:I[t].nonEventCountArray,stack:"event"});for(var o=1;o<=I[t].eventCountArray.length;o++)a.xAxis.data.push(o);k[t]=a,i.series.push({name:"woe",type:"bar",data:I[t].woeArray}),i.series.push({name:"",type:"line",data:I[t].woeArray}),T[t]=i,w.push({variable:t,iv:I[t].iv,monotonicity:I[t].isWoeMonotonic?I[t].isWoeMonotonic.toString():""}),O.push({value:t,label:t})}),e.modelOutput={sourceData:w,options:O,variableData:C,stackBarData:k,woeData:T}}else t.includes("sample")?e.modelOutputType="sample":t.includes("hot")?e.modelOutputType="hot":t.includes("evaluation")&&(e.modelOutputType="evaluation")})}}},I=(n("Bw9L"),Object(b.a)(T,function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"app-container details-container bg-dark"},[n("h3",{staticClass:"app-title"},[n("span",{staticClass:"text-primary pointer",on:{click:t.toPrevPage}},[t._v(t._s(t.jobFrom))]),t._v(" > "+t._s(t.jobId)+"\n  ")]),t._v(" "),n("section",{staticClass:"section-wrapper"},[n("div",{staticClass:"flex space-between",staticStyle:{"padding-top":"12px"}},[n("h3",{staticClass:"section-title"},[t._v("JOB SUMMARY")]),t._v(" "),n("el-button",{staticStyle:{transform:"translateY(-12px)"},attrs:{type:"primary",round:""},on:{click:function(e){t.$router.push({path:"/dashboard",query:{jobId:t.jobId}})}}},[t._v("dashboard\n      ")])],1),t._v(" "),n("div",{directives:[{name:"loading",rawName:"v-loading",value:t.summaryLoading,expression:"summaryLoading"}],staticClass:"section-view job-summary shadow"},[n("el-row",[n("el-col",{staticClass:"section-col",attrs:{span:6}},[n("div",{staticClass:"text-col"},[n("p",{staticClass:"prop"},[t._v("Job ID")]),t._v(" "),n("p",{staticClass:"prop"},[t._v("Status")])]),t._v(" "),n("div",{staticClass:"text-col"},[n("p",{staticClass:"value",staticStyle:{color:"#494ece"}},[t._v(t._s(t.jobId))]),t._v(" "),n("p",{staticClass:"value",staticStyle:{color:"#494ece"}},[t._v(t._s(t.status))])])]),t._v(" "),n("el-col",{staticClass:"section-col",attrs:{span:9}},[n("div",{staticClass:"text-col"},[n("p",{staticClass:"prop"},[t._v("Guest")]),t._v(" "),n("p",{staticClass:"prop",staticStyle:{"margin-top":"34px"}},[t._v("Host")])]),t._v(" "),n("div",[n("div",{staticClass:"flex"},[n("div",{staticClass:"text-col"},[n("p",{staticClass:"value"},[t._v(t._s(t.guest.dataset))]),t._v(" "),n("p",{staticClass:"value"},[t._v(t._s(t.guest.columns))])]),t._v(" "),n("div",{staticClass:"text-col"},[n("p",{staticClass:"value"},[t._v(t._s(t.guest.target))]),t._v(" "),n("p",{staticClass:"value"},[t._v(t._s(t.guest.rows))])])]),t._v(" "),n("div",{staticClass:"flex"},[n("div",{staticClass:"text-col"},[n("p",{staticClass:"value"},[t._v(t._s(t.guest.partner))]),t._v(" "),n("p",{staticClass:"value"},[t._v(t._s(t.guest.pnr_dataset))])])])])]),t._v(" "),n("el-col",{staticClass:"section-col",attrs:{span:9}},[n("div",{staticClass:"text-col"},[n("p",{staticClass:"prop"},[t._v("Submission Time")]),t._v(" "),n("p",{staticClass:"prop"},[t._v("Start Time")]),t._v(" "),n("p",{staticClass:"prop"},[t._v("End time")]),t._v(" "),n("p",{staticClass:"prop"},[t._v("Duration")])]),t._v(" "),n("div",{staticClass:"text-col"},[n("p",{staticClass:"value"},[t._v(t._s(t.jobInfo.submmissionTime))]),t._v(" "),n("p",{staticClass:"value"},[t._v(t._s(t.jobInfo.startTime))]),t._v(" "),n("p",{staticClass:"value"},[t._v(t._s(t.jobInfo.endTime))]),t._v(" "),n("p",{staticClass:"value"},[t._v(t._s(t.jobInfo.duration))])])])],1)],1)]),t._v(" "),n("section",{staticClass:"section-wrapper"},[n("h3",{staticClass:"section-title"},[t._v("OUTPUTS FROM JOB")]),t._v(" "),n("div",{staticClass:"section-view shadow flex"},[n("div",{staticClass:"output-wrapper"},[t._m(0),t._v(" "),n("echart-container",{class:"echarts",attrs:{options:t.graphOptions},on:{getEchartInstance:t.getGraphEchartInstance}})],1),t._v(" "),n("div",{directives:[{name:"loading",rawName:"v-loading",value:t.paraLoading,expression:"paraLoading"}],staticClass:"output-wrapper"},[n("h4",{staticClass:"output-title"},[t._v("statistics")]),t._v(" "),n("div",{directives:[{name:"loading",rawName:"v-loading",value:t.msgLoading,expression:"msgLoading"}],staticClass:"msg"},[n("div",{directives:[{name:"show",rawName:"v-show",value:t.paraList.length>0,expression:"paraList.length>0"}],staticClass:"flex"},[n("h4",{staticClass:"msg-title"},[t._v("Parameters("+t._s(t.paraList.length)+")")]),t._v(" "),n("ul",{staticClass:"para-list"},t._l(t.paraList,function(e,a){return n("li",{key:a},[t._v(t._s(e))])}))])])])])]),t._v(" "),t.modelSummaryData?n("section",{staticClass:"section-wrapper"},[n("h3",{staticClass:"section-title"},[t._v("MODEL SUMMARY")]),t._v(" "),n("div",{staticClass:"section-view flex shadow"},[n("el-table",{staticStyle:{width:"100%"},attrs:{data:t.modelSummaryData.data}},t._l(t.modelSummaryData.header,function(t,e){return n("el-table-column",{key:e,attrs:{prop:t.prop,label:t.label,"show-overflow-tooltip":"",align:"center"}})}))],1)]):t._e(),t._v(" "),n("el-dialog",{attrs:{visible:t.outputVisible,title:t.outputTitle,width:"80%",top:"10vh"},on:{"update:visible":function(e){t.outputVisible=e},open:function(e){t.modelOutputLoading=!0},close:t.initOutput}},[n("section",{directives:[{name:"loading",rawName:"v-loading",value:t.modelOutputLoading,expression:"modelOutputLoading"}],staticClass:"section-wrapper",staticStyle:{padding:"0"}},[n("div",{staticClass:"section-view",staticStyle:{padding:"0"}},[n("div",{staticClass:"tab-bar flex"},[n("div",{staticClass:"tab-btn",class:{"tab-btn-active":"model"===t.currentTab},on:{click:function(e){t.switchLogTab("model")}}},[n("span",{staticClass:"text"},[t._v("model_output")])]),t._v(" "),n("div",{staticClass:"tab-btn",class:{"tab-btn-active":"data"===t.currentTab},on:{click:function(e){t.switchLogTab("data")}}},[n("span",{staticClass:"text"},[t._v("data_output")])]),t._v(" "),n("div",{staticClass:"tab-btn",class:{"tab-btn-active":"log"===t.currentTab},on:{click:function(e){t.switchLogTab("log")}}},[n("span",{staticClass:"text"},[t._v("log")])])]),t._v(" "),n("div",{directives:[{name:"show",rawName:"v-show",value:"model"===t.currentTab,expression:"currentTab === 'model'"}],staticClass:"tab"},[n("model-output",{attrs:{"metric-output-list":t.metricOutputList,"model-output-type":t.modelOutputType,"model-output":t.modelOutput}})],1),t._v(" "),n("div",{directives:[{name:"show",rawName:"v-show",value:"data"===t.currentTab,expression:"currentTab === 'data'"}]},[n("data-output",{attrs:{"t-header":t.dataOutputHeader,"t-body":t.dataOutputBody}})],1),t._v(" "),n("div",{directives:[{name:"show",rawName:"v-show",value:"log"===t.currentTab,expression:"currentTab === 'log'"}]},[n("ul",{staticClass:"log-list"},t._l(t.logList,function(e,a){return n("li",{key:a},[n("span",{staticStyle:{color:"#999","margin-right":"5px"}},[t._v(t._s(e.lineNum))]),t._v("\n              "+t._s(e.content)+"\n            ")])}))])])])])],1)},[function(){var t=this.$createElement,e=this._self._c||t;return e("h4",{staticClass:"output-title"},[this._v("main graph"),e("span",[this._v("Click component to view details")])])}],!1,null,null,null));I.options.__file="index.vue";e.default=I.exports},rx6N:function(t,e,n){(t.exports=n("I1BE")(!1)).push([t.i,"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n",""])}}]);