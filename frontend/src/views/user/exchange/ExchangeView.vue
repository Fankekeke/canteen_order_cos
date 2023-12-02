<template>
  <a-modal v-model="show" title="兑换详情" @cancel="onClose" :width="1000">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei">
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="exchangeData !== null">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">兑换信息</span></a-col>
        <a-col :span="8"><b>兑换时间：</b>
          {{ exchangeData.createDate }}
        </a-col>
        <a-col :span="8"><b>消耗积分：</b>
          {{ exchangeData.integral ? exchangeData.integral : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <div v-if="userInfo != null">
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">用户信息</span></a-col>
          <a-col :span="8"><b>用户编号：</b>
            {{ userInfo.code }}
          </a-col>
          <a-col :span="8"><b>用户名称：</b>
            {{ userInfo.name ? userInfo.name : '- -' }}
          </a-col>
          <a-col :span="8"><b>联系方式：</b>
            {{ userInfo.phone ? userInfo.phone : '- -' }}
          </a-col>
        </a-row>
        <br/>
        <a-row style="padding-left: 24px;padding-right: 24px;">
          <a-col :span="8"><b>收货地址：</b>
            {{ userInfo.address }}
          </a-col>
        </a-row>
      </div>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="materialInfo != null">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">物品信息</span></a-col>
        <a-col :span="8"><b>物品名称：</b>
          {{ materialInfo.name }}
        </a-col>
        <a-col :span="8"><b>物品编号：</b>
          {{ materialInfo.code ? materialInfo.code : '- -' }}
        </a-col>
        <a-col :span="8"><b>销量：</b>
          {{ materialInfo.saleNum ? materialInfo.saleNum : '- -' }}
        </a-col>
        <br/>
        <br/>
        <a-col :span="24"><b></b>
          {{ materialInfo.content ? materialInfo.content : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">图册</span></a-col>
        <a-col :span="24">
          <a-upload
            name="avatar"
            action="http://127.0.0.1:9527/file/fileUpload/"
            list-type="picture-card"
            :file-list="fileList"
            @preview="handlePreview"
            @change="picHandleChange"
          >
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
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
  name: 'exchangeView',
  props: {
    exchangeShow: {
      type: Boolean,
      default: false
    },
    exchangeData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.exchangeShow
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
      userInfo: null,
      exchangeInfo: null,
      materialInfo: null
    }
  },
  watch: {
    exchangeShow: function (value) {
      if (value) {
        this.changeDetail(this.exchangeData.id)
      }
    }
  },
  methods: {
    changeDetail (id) {
      this.$get(`/cos/exchange-info/${id}`).then((r) => {
        this.userInfo = r.data.user
        this.exchangeInfo = r.data.exchange
        this.materialInfo = r.data.material
        this.imagesInit(this.materialInfo.images)
      })
    },
    local (exchangeData) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(exchangeData.longitude, exchangeData.latitude)
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
