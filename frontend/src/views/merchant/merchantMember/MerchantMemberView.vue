<template>
  <a-modal v-model="show" title="商家会员详情" @cancel="onClose" :width="1000">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="exchangeData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">会员信息</span></a-col>
        <a-col :span="8"><b>会员编号：</b>
          {{ exchangeData.userCode }}
        </a-col>
        <a-col :span="8"><b>会员名称：</b>
          {{ exchangeData.userName ? exchangeData.userName : '- -' }}
        </a-col>
        <a-col :span="8"><b>商家名称：</b>
          {{ exchangeData.merchantName ? exchangeData.merchantName : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>商家地址：</b>
          {{ exchangeData.address }}
        </a-col>
        <a-col :span="8"><b>负责人：</b>
          {{ exchangeData.principal ? exchangeData.principal : '- -' }}
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          {{ exchangeData.phone ? exchangeData.phone : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>消费金额：</b>
          {{ exchangeData.consumption }} 元
        </a-col>
        <a-col :span="8"><b>最后消费时间：</b>
          {{ exchangeData.lastDate ? exchangeData.lastDate : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">购买菜品</span></a-col>
        <a-col :span="24">
          <a-table :columns="columns" :data-source="orderList">
          </a-table>
        </a-col>
      </a-row>
      <br/>
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
    },
    columns () {
      return [{
        title: '订单编号',
        dataIndex: 'code'
      }, {
        title: '订单价格',
        dataIndex: 'orderPrice'
      }, {
        title: '折后价格',
        dataIndex: 'afterOrderPrice'
      }, {
        title: '订单类型',
        dataIndex: 'type',
        customRender: (text, row, index) => {
          switch (text) {
            case '0':
              return <a-tag>堂食</a-tag>
            case '1':
              return <a-tag>外送</a-tag>
            default:
              return '- -'
          }
        }
      }, {
        title: '下单时间',
        dataIndex: 'createDate',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      },]
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
      orderList: []
    }
  },
  watch: {
    exchangeShow: function (value) {
      if (value) {
        this.changeDetail(this.exchangeData.userId)
      }
    }
  },
  methods: {
    changeDetail (userId) {
      this.$get(`/cos/order-info/selectMerchantMember/${userId}`).then((r) => {
        this.orderList = r.data.data
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
