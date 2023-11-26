<template>
  <a-modal v-model="show" title="商家详情" @cancel="onClose" :width="1000">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="merchantData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>商家编号：</b>
          {{ merchantData.code }}
        </a-col>
        <a-col :span="8"><b>商家名称：</b>
          {{ merchantData.name ? merchantData.name : '- -' }}
        </a-col>
        <a-col :span="8"><b>负责人：</b>
          {{ merchantData.principal ? merchantData.principal : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>联系方式：</b>
          {{ merchantData.phone }}
        </a-col>
        <a-col :span="16"><b>详细地址：</b>
          {{ merchantData.address }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>经度：</b>
          {{ merchantData.longitude }}
        </a-col>
        <a-col :span="8"><b>纬度：</b>
          {{ merchantData.latitude }}
        </a-col>
        <a-col :span="8"><b>所属菜系：</b>
          {{ merchantData.dishes }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="16"><b>商家信息：</b>
          {{ merchantData.content }}
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ merchantData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>创建时间：</b>
          {{ merchantData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>营业星期：</b>
          {{ merchantData.operateDay }}
        </a-col>
        <a-col :span="8"><b>开始营业时间：</b>
          {{ merchantData.operateStartTime }}
        </a-col>
        <a-col :span="8"><b>营业结束时间：</b>
          {{ merchantData.operateEndTime }}
        </a-col>
      </a-row>
      <br/>
      <br/>
       <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 5px"><span style="font-size: 15px;font-weight: 650;color: #000c17">地址位置</span></a-col>
      </a-row>
      <div>
        <a-card :bordered="false" style="height: 500px">
          <div id="areas" style="width: 100%;height: 450px;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);background:#ec9e3c;color:#fff"></div>
        </a-card>
      </div>
    </div>
  </a-modal>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
import moment from 'moment'
moment.locale('zh-cn')
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'merchantView',
  props: {
    merchantShow: {
      type: Boolean,
      default: false
    },
    merchantData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.merchantShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      repairInfo: null,
      reserveInfo: null,
      durgList: [],
      logisticsList: [],
      userInfo: null
    }
  },
  watch: {
    merchantShow: function (value) {
      if (value) {
        this.imagesInit(this.merchantData.images)
        setTimeout(() => {
          baiduMap.initMap('areas')
          setTimeout(() => {
            this.local(this.merchantData)
          }, 500)
        }, 200)
      }
    }
  },
  methods: {
    local (merchantData) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(merchantData.longitude, merchantData.latitude)
      baiduMap.pointAdd(point)
      baiduMap.findPoint(point, 16)
      // let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions:{map: baiduMap.rMap(), autoViewport: true}});
      // driving.search(new BMap.Point(this.nowPoint.lng,this.nowPoint.lat), new BMap.Point(scenic.point.split(",")[0],scenic.point.split(",")[1]));
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
