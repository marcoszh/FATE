(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-af5e"],{"+A8E":function(t,e,n){var r=n("eAVi");"string"==typeof r&&(r=[[t.i,r,""]]),r.locals&&(t.exports=r.locals);(0,n("SZ7m").default)("5dcdc66d",r,!0,{})},"+iuc":function(t,e,n){n("wgeU"),n("FlQf"),n("bBy9"),n("B9jh"),n("dL40"),n("xvv9"),n("V+O7"),t.exports=n("WEpk").Set},"/Qlk":function(t,e,n){"use strict";var r=n("idaj");n.n(r).a},"0AMJ":function(t,e,n){"use strict";var r=n("+A8E");n.n(r).a},"0tVQ":function(t,e,n){n("FlQf"),n("VJsP"),t.exports=n("WEpk").Array.from},"7Qib":function(t,e,n){"use strict";n.d(e,"b",function(){return o}),n.d(e,"a",function(){return a});n("jWXv"),n("rfXi"),n("gDS+"),n("GQeE");var r=n("EJiy"),i=n.n(r);function o(t,e){if(0===arguments.length)return null;var n=e||"{y}-{m}-{d} {h}:{i}:{s}",r=void 0;"object"===(void 0===t?"undefined":i()(t))?r=t:("string"==typeof t&&/^[0-9]+$/.test(t)&&(t=parseInt(t)),"number"==typeof t&&10===t.toString().length&&(t*=1e3),r=new Date(t));var o={y:r.getFullYear(),m:r.getMonth()+1,d:r.getDate(),h:r.getHours(),i:r.getMinutes(),s:r.getSeconds(),a:r.getDay()};return n.replace(/{(y|m|d|h|i|s|a)+}/g,function(t,e){var n=o[e];return"a"===e?["日","一","二","三","四","五","六"][n]:(t.length>0&&n<10&&(n="0"+n),n||0)})}function a(t){var e=Math.floor(t/3600),n=Math.floor(t/60%60),r=Math.floor(t%60),i=function(t){return t<1?"00":t<10?"0"+t:t.toString()};return(e=i(e))+":"+(n=i(n))+":"+(r=i(r))}},"8iia":function(t,e,n){var r=n("QMMT"),i=n("RRc/");t.exports=function(t){return function(){if(r(this)!=t)throw TypeError(t+"#toJSON isn't generic");return i(this)}}},B9jh:function(t,e,n){"use strict";var r=n("Wu5q"),i=n("n3ko");t.exports=n("raTm")("Set",function(t){return function(){return t(this,arguments.length>0?arguments[0]:void 0)}},{add:function(t){return r.def(i(this,"Set"),t=0===t?0:t,t)}},r)},C2SN:function(t,e,n){var r=n("93I4"),i=n("kAMH"),o=n("UWiX")("species");t.exports=function(t){var e;return i(t)&&("function"!=typeof(e=t.constructor)||e!==Array&&!i(e.prototype)||(e=void 0),r(e)&&null===(e=e[o])&&(e=void 0)),void 0===e?Array:e}},GQeE:function(t,e,n){t.exports={default:n("iq4v"),__esModule:!0}},IP1Z:function(t,e,n){"use strict";var r=n("2faE"),i=n("rr1i");t.exports=function(t,e,n){e in t?r.f(t,e,i(0,n)):t[e]=n}},Mqbl:function(t,e,n){var r=n("JB68"),i=n("w6GO");n("zn7N")("keys",function(){return function(t){return i(r(t))}})},"RRc/":function(t,e,n){var r=n("oioR");t.exports=function(t,e){var n=[];return r(t,!1,n.push,n,e),n}},"V+O7":function(t,e,n){n("aPfg")("Set")},V7Et:function(t,e,n){var r=n("2GTP"),i=n("M1xp"),o=n("JB68"),a=n("tEej"),u=n("v6xn");t.exports=function(t,e){var n=1==t,s=2==t,c=3==t,f=4==t,l=6==t,d=5==t||l,p=e||u;return function(e,u,g){for(var h,v,m=o(e),b=i(m),y=r(u,g,3),_=a(b.length),w=0,S=n?p(e,_):s?p(e,0):void 0;_>w;w++)if((d||w in b)&&(v=y(h=b[w],w,m),t))if(n)S[w]=v;else if(v)switch(t){case 3:return!0;case 5:return h;case 6:return w;case 2:S.push(h)}else if(f)return!1;return l?-1:c||f?f:S}}},VJsP:function(t,e,n){"use strict";var r=n("2GTP"),i=n("Y7ZC"),o=n("JB68"),a=n("sNwI"),u=n("NwJ3"),s=n("tEej"),c=n("IP1Z"),f=n("fNZA");i(i.S+i.F*!n("TuGD")(function(t){Array.from(t)}),"Array",{from:function(t){var e,n,i,l,d=o(t),p="function"==typeof this?this:Array,g=arguments.length,h=g>1?arguments[1]:void 0,v=void 0!==h,m=0,b=f(d);if(v&&(h=r(h,g>2?arguments[2]:void 0,2)),void 0==b||p==Array&&u(b))for(n=new p(e=s(d.length));e>m;m++)c(n,m,v?h(d[m],m):d[m]);else for(l=b.call(d),n=new p;!(i=l.next()).done;m++)c(n,m,v?a(l,h,[i.value,m],!0):i.value);return n.length=m,n}})},Wu5q:function(t,e,n){"use strict";var r=n("2faE").f,i=n("oVml"),o=n("XJU/"),a=n("2GTP"),u=n("EXMj"),s=n("oioR"),c=n("MPFp"),f=n("UO39"),l=n("TJWN"),d=n("jmDH"),p=n("6/1s").fastKey,g=n("n3ko"),h=d?"_s":"size",v=function(t,e){var n,r=p(e);if("F"!==r)return t._i[r];for(n=t._f;n;n=n.n)if(n.k==e)return n};t.exports={getConstructor:function(t,e,n,c){var f=t(function(t,r){u(t,f,e,"_i"),t._t=e,t._i=i(null),t._f=void 0,t._l=void 0,t[h]=0,void 0!=r&&s(r,n,t[c],t)});return o(f.prototype,{clear:function(){for(var t=g(this,e),n=t._i,r=t._f;r;r=r.n)r.r=!0,r.p&&(r.p=r.p.n=void 0),delete n[r.i];t._f=t._l=void 0,t[h]=0},delete:function(t){var n=g(this,e),r=v(n,t);if(r){var i=r.n,o=r.p;delete n._i[r.i],r.r=!0,o&&(o.n=i),i&&(i.p=o),n._f==r&&(n._f=i),n._l==r&&(n._l=o),n[h]--}return!!r},forEach:function(t){g(this,e);for(var n,r=a(t,arguments.length>1?arguments[1]:void 0,3);n=n?n.n:this._f;)for(r(n.v,n.k,this);n&&n.r;)n=n.p},has:function(t){return!!v(g(this,e),t)}}),d&&r(f.prototype,"size",{get:function(){return g(this,e)[h]}}),f},def:function(t,e,n){var r,i,o=v(t,e);return o?o.v=n:(t._l=o={i:i=p(e,!0),k:e,v:n,p:r=t._l,n:void 0,r:!1},t._f||(t._f=o),r&&(r.n=o),t[h]++,"F"!==i&&(t._i[i]=o)),t},getEntry:v,setStrong:function(t,e,n){c(t,e,function(t,n){this._t=g(t,e),this._k=n,this._l=void 0},function(){for(var t=this._k,e=this._l;e&&e.r;)e=e.p;return this._t&&(this._l=e=e?e.n:this._t._f)?f(0,"keys"==t?e.k:"values"==t?e.v:[e.k,e.v]):(this._t=void 0,f(1))},n?"entries":"values",!n,!0),l(e)}}},aPfg:function(t,e,n){"use strict";var r=n("Y7ZC"),i=n("eaoh"),o=n("2GTP"),a=n("oioR");t.exports=function(t){r(r.S,t,{from:function(t){var e,n,r,u,s=arguments[1];return i(this),(e=void 0!==s)&&i(s),void 0==t?new this:(n=[],e?(r=0,u=o(s,arguments[2],2),a(t,!1,function(t){n.push(u(t,r++))})):a(t,!1,n.push,n),new this(n))}})}},bElp:function(t,e,n){(t.exports=n("I1BE")(!1)).push([t.i,".history-container .table-wrapper[data-v-9d6babfa] {\n  height: 70vh;\n}\n",""])},cHUd:function(t,e,n){"use strict";var r=n("Y7ZC");t.exports=function(t){r(r.S,t,{of:function(){for(var t=arguments.length,e=new Array(t);t--;)e[t]=arguments[t];return new this(e)}})}},dL40:function(t,e,n){var r=n("Y7ZC");r(r.P+r.R,"Set",{toJSON:n("8iia")("Set")})},dv4G:function(t,e,n){"use strict";n.d(e,"a",function(){return i}),n.d(e,"b",function(){return o}),n.d(e,"g",function(){return a}),n.d(e,"e",function(){return u}),n.d(e,"d",function(){return s}),n.d(e,"c",function(){return c}),n.d(e,"f",function(){return f}),n.d(e,"h",function(){return l});var r=n("t3Un");function i(t){return Object(r.a)({url:"/job/query/all",method:"get",params:t})}function o(t){return Object(r.a)({url:"/job/query/status",method:"get",params:t})}function a(t){return Object(r.a)({url:"/job/v1/pipeline/job/stop",method:"post",data:{job_id:t}})}function u(t){return Object(r.a)({url:"/job/query/"+t,method:"get"})}function s(t){return Object(r.a)({url:"/v1/pipeline/dag/dependencies",method:"post",data:{job_id:t}})}function c(t){return Object(r.a)({url:"/v1/tracking/component/parameters",method:"post",data:t})}function f(t){return Object(r.a)({url:"/v1/tracking/component/output/model",method:"post",data:t})}function l(t){var e=t.componentId,n=t.jobId,i=t.begin,o=t.end;return Object(r.a)({url:"/queryLogWithSize/"+e+"/"+n+"/"+i+"/"+o+"  ",method:"get"})}},eAVi:function(t,e,n){(t.exports=n("I1BE")(!1)).push([t.i,"\n.pagination-container[data-v-006d6ecc] {\n  background: #fff;\n  padding: 32px 16px;\n}\n.pagination-container.hidden[data-v-006d6ecc] {\n  display: none;\n}\n",""])},"gDS+":function(t,e,n){t.exports={default:n("oh+g"),__esModule:!0}},idaj:function(t,e,n){var r=n("bElp");"string"==typeof r&&(r=[[t.i,r,""]]),r.locals&&(t.exports=r.locals);(0,n("SZ7m").default)("3f65fa5f",r,!0,{})},iq4v:function(t,e,n){n("Mqbl"),t.exports=n("WEpk").Object.keys},jWXv:function(t,e,n){t.exports={default:n("+iuc"),__esModule:!0}},"k/PY":function(t,e,n){"use strict";n.r(e);Math.easeInOutQuad=function(t,e,n,r){return(t/=r/2)<1?n/2*t*t+e:-n/2*(--t*(t-2)-1)+e};var r=window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame||function(t){window.setTimeout(t,1e3/60)};function i(t,e,n){var i=document.documentElement.scrollTop||document.body.parentNode.scrollTop||document.body.scrollTop,o=t-i,a=0;e=void 0===e?500:e;!function t(){a+=20,function(t){document.documentElement.scrollTop=t,document.body.parentNode.scrollTop=t,document.body.scrollTop=t}(Math.easeInOutQuad(a,i,o,e)),a<e?r(t):n&&"function"==typeof n&&n()}()}var o={name:"Pagination",props:{total:{required:!0,type:Number},page:{type:Number,default:1},limit:{type:Number,default:20},pageSizes:{type:Array,default:function(){return[10,20,30,50]}},layout:{type:String,default:"total, sizes, prev, pager, next, jumper"},background:{type:Boolean,default:!0},autoScroll:{type:Boolean,default:!0},hidden:{type:Boolean,default:!1}},computed:{currentPage:{get:function(){return this.page},set:function(t){this.$emit("update:page",t)}},pageSize:{get:function(){return this.limit},set:function(t){this.$emit("update:limit",t)}}},methods:{handleSizeChange:function(t){this.$emit("pagination",{page:this.currentPage,limit:t}),this.autoScroll&&i(0,800)},handleCurrentChange:function(t){this.$emit("pagination",{page:t,limit:this.pageSize}),this.autoScroll&&i(0,800)}}},a=(n("0AMJ"),n("KHd+")),u=Object(a.a)(o,function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"pagination-container flex flex-end",class:{hidden:t.hidden}},[n("el-pagination",t._b({attrs:{background:t.background,"current-page":t.currentPage,"page-size":t.pageSize,layout:t.layout,"page-sizes":t.pageSizes,total:t.total},on:{"update:currentPage":function(e){t.currentPage=e},"update:pageSize":function(e){t.pageSize=e},"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}},"el-pagination",t.$attrs,!1))],1)},[],!1,null,"006d6ecc",null);u.options.__file="index.vue";var s=u.exports,c=n("7Qib"),f=n("dv4G"),l={name:"Job",components:{Pagination:s},filters:{formatType:function(t){var e="未知";switch(t){case 1:e="intersection";break;case 2:e="feature engineering";break;case 3:e="model training";break;case 4:e="model prdiction"}return e}},data:function(){return{list:null,tHead:[{key:"jobId",label:"jobId"},{key:"dataset",label:"DATASET"},{key:"partner",label:"PARTNER"},{key:"pnr_dataset",label:"PNR-DATASET",width:180},{key:"start_time",label:"START TIME",width:180},{key:"end_time",label:"END TIME",width:180},{key:"duration",label:"DURATION",width:150},{key:"status",label:"STATUS",width:150},{key:"progress",hidden:!0,width:150}],listLoading:!0,pageSize:10,total:0,page:1,dialogVisible:!1,formLoading:!1,form:{experiment:"",type:"",desc:""},formRules:{experiment:[{required:!0,message:"Please enter your name",trigger:"blur"}],type:[{required:!0,message:"Please enter your name",trigger:"blur"}],desc:[{required:!0,message:"Please enter a description",trigger:"blur"}]}}},mounted:function(){this.getList()},methods:{getList:function(){var t=this;Object(f.a)().then(function(e){t.listLoading=!1;var n=t.page,r=[];e.data.forEach(function(t){var e=t.job,n=t.dataset;r.push({jobId:e.fJobId,dataset:n.dataset,partner:n.partner,pnr_dataset:n.pnr_dataset,start_time:Object(c.b)(new Date(e.fStartTime)),end_time:Object(c.b)(e.fEndTime),duration:Object(c.b)(e.fStartTime,"{h}:{i}:{s}"),status:e.fStatus,progress:"running"===e.fStatus?e.fProgress:null})}),Array.isArray(r)&&(t.total=r.length,r=r.filter(function(e,r){return r<t.pageSize*n&&r>=t.pageSize*(n-1)}),console.log(r),t.list=r)})},deleteExp:function(t){this.$message({message:"删除成功"})},toDetailes:function(t){this.$router.push({path:"/details",query:{jobId:t,from:"Job overview"}})}}},d=(n("/Qlk"),Object(a.a)(l,function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"app-container history-container"},[n("div",{staticClass:"app-top flex space-between flex-center"},[n("el-breadcrumb",{attrs:{"separator-class":"el-icon-arrow-right text-black"}},[n("el-breadcrumb-item",[t._v("Job overview")])],1)],1),t._v(" "),n("div",{staticClass:"table-wrapper"},[n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.listLoading,expression:"listLoading"}],attrs:{data:t.list,"element-loading-text":"Loading","highlight-current-row":"",height:"70vh"}},[t._l(t.tHead,function(e){return[e.hidden?t._e():n("el-table-column",{key:e.key,attrs:{prop:e.key,label:e.label,sortable:e.sortable,"show-overflow-tooltip":"",align:"center",border:""},scopedSlots:t._u([{key:"default",fn:function(r){return["jobId"===e.key?n("span",{staticClass:"text-primary pointer",on:{click:function(n){t.toDetailes(r.row[e.key])}}},[t._v(t._s(r.row[e.key]))]):"status"===e.key&&r.row.progress?n("el-progress",{attrs:{percentage:r.row.progress,color:"#67c23a"}}):n("span",[t._v(t._s(r.row[e.key]))])]}}])})]})],2),t._v(" "),n("pagination",{directives:[{name:"show",rawName:"v-show",value:t.total>0,expression:"total>0"}],attrs:{total:t.total,page:t.page,limit:t.pageSize},on:{"update:page":function(e){t.page=e},"update:limit":function(e){t.pageSize=e},pagination:t.getList}})],1)])},[],!1,null,"9d6babfa",null));d.options.__file="index.vue";e.default=d.exports},n3ko:function(t,e,n){var r=n("93I4");t.exports=function(t,e){if(!r(t)||t._t!==e)throw TypeError("Incompatible receiver, "+e+" required!");return t}},"oh+g":function(t,e,n){var r=n("WEpk"),i=r.JSON||(r.JSON={stringify:JSON.stringify});t.exports=function(t){return i.stringify.apply(i,arguments)}},raTm:function(t,e,n){"use strict";var r=n("5T2Y"),i=n("Y7ZC"),o=n("6/1s"),a=n("KUxP"),u=n("NegM"),s=n("XJU/"),c=n("oioR"),f=n("EXMj"),l=n("93I4"),d=n("RfKB"),p=n("2faE").f,g=n("V7Et")(0),h=n("jmDH");t.exports=function(t,e,n,v,m,b){var y=r[t],_=y,w=m?"set":"add",S=_&&_.prototype,k={};return h&&"function"==typeof _&&(b||S.forEach&&!a(function(){(new _).entries().next()}))?(_=e(function(e,n){f(e,_,t,"_c"),e._c=new y,void 0!=n&&c(n,m,e[w],e)}),g("add,clear,delete,forEach,get,has,set,keys,values,entries,toJSON".split(","),function(t){var e="add"==t||"set"==t;t in S&&(!b||"clear"!=t)&&u(_.prototype,t,function(n,r){if(f(this,_,t),!e&&b&&!l(n))return"get"==t&&void 0;var i=this._c[t](0===n?0:n,r);return e?this:i})}),b||p(_.prototype,"size",{get:function(){return this._c.size}})):(_=v.getConstructor(e,t,m,w),s(_.prototype,n),o.NEED=!0),d(_,t),k[t]=_,i(i.G+i.W+i.F,k),b||v.setStrong(_,t,m),_}},rfXi:function(t,e,n){t.exports={default:n("0tVQ"),__esModule:!0}},v6xn:function(t,e,n){var r=n("C2SN");t.exports=function(t,e){return new(r(t))(e)}},xvv9:function(t,e,n){n("cHUd")("Set")},zn7N:function(t,e,n){var r=n("Y7ZC"),i=n("WEpk"),o=n("KUxP");t.exports=function(t,e){var n=(i.Object||{})[t]||Object[t],a={};a[t]=e(n),r(r.S+r.F*o(function(){n(1)}),"Object",a)}}}]);