<template>
  <a-modal v-model="show" title="用户地址详情" @cancel="onClose" :width="1000">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="addressData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>地址编号：</b>
          {{ addressData.code }}
        </a-col>
        <a-col :span="8"><b>省份：</b>
          {{ addressData.province ? addressData.province : '- -' }}
        </a-col>
        <a-col :span="8"><b>城市：</b>
          {{ addressData.city ? addressData.city : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>区：</b>
          {{ addressData.area }}
        </a-col>
        <a-col :span="8"><b>联系人：</b>
          {{ addressData.contactPerson }}
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          {{ addressData.contactMethod }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>经度：</b>
          {{ addressData.longitude }}
        </a-col>
        <a-col :span="8"><b>纬度：</b>
          {{ addressData.latitude }}
        </a-col>
        <a-col :span="8"><b>所属用户：</b>
          {{ addressData.name }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="16"><b>具体位置：</b>
          {{ addressData.address }}
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ addressData.createDate }}
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
  name: 'addressView',
  props: {
    addressShow: {
      type: Boolean,
      default: false
    },
    addressData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.addressShow
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
    addressShow: function (value) {
      if (value) {
        this.imagesInit(this.addressData.images)
        setTimeout(() => {
          baiduMap.initMap('areas')
          setTimeout(() => {
            this.local(this.addressData)
          }, 500)
        }, 200)
      }
    }
  },
  methods: {
    local (addressData) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(addressData.longitude, addressData.latitude)
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
