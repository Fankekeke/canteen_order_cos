<template>
  <a-modal v-model="show" title="订单处理" @cancel="onClose" :width="1200">
    <template slot="footer">
      <a-button key="back" @click="checkDealer" type="primary">
        分配
      </a-button>
      <a-button @click="onClose">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="orderInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">订单信息</span></a-col>
        <a-col :span="6"><b>订单编号：</b>
          {{ orderInfo.code }}
        </a-col>
        <a-col :span="6"><b>订单价格：</b>
          {{ orderInfo.orderPrice ? orderInfo.orderPrice + '元' : '- -' }}
        </a-col>
        <a-col :span="6"><b>折后价格：</b>
          {{ orderInfo.afterOrderPrice ? orderInfo.afterOrderPrice + '元' : '- -' }}
        </a-col>
        <a-col :span="6"><b>会员折扣：</b>
          {{ orderInfo.discount }} 元
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="6"><b>订单积分：</b>
          {{ orderInfo.integral }}
        </a-col>
        <a-col :span="6"><b>订单状态：</b>
          <span v-if="orderInfo.status === '0'" style="color: red">未支付</span>
          <span v-if="orderInfo.status === '1'" style="color: blue">已支付</span>
          <span v-if="orderInfo.status === '2'" style="color: orange">配送中</span>
          <span v-if="orderInfo.status === '3'" style="color: green">已收货</span>
        </a-col>
        <a-col :span="6"><b>订单类型：</b>
          <span v-if="orderInfo.type === '0'">堂食</span>
          <span v-if="orderInfo.type === '1'">外送</span>
        </a-col>
        <a-col :span="6"><b>下单时间：</b>
          {{ orderInfo.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="orderInfo.type === '1'">
        <a-col :span="6"><b>公里数：</b>
          {{ orderInfo.kilometre }}公里
        </a-col>
        <a-col :span="6"><b>配送价格：</b>
          {{ orderInfo.distributionPrice ? orderInfo.distributionPrice + '元' : '- -' }}
        </a-col>
        <a-col :span="6"><b>支付时间：</b>
          {{ orderInfo.payDate ? orderInfo.payDate : '- -' }}
        </a-col>
        <a-col :span="6"><b>送达时间：</b>
          {{ orderInfo.serviceDate }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="userInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">用户信息</span></a-col>
        <a-col :span="6"><b>会员编号：</b>
          {{ userInfo.code }}
        </a-col>
        <a-col :span="6"><b>用户姓名：</b>
          {{ userInfo.name ? userInfo.name : '- -' }}
        </a-col>
        <a-col :span="6"><b>邮箱地址：</b>
          {{ userInfo.mail ? userInfo.mail : '- -' }}
        </a-col>
        <a-col :span="6"><b>联系电话：</b>
          {{ userInfo.phone }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="merchantInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">商家信息</span></a-col>
        <a-col :span="6"><b>商家编号：</b>
          {{ merchantInfo.code }}
        </a-col>
        <a-col :span="6"><b>商家名称：</b>
          {{ merchantInfo.name ? merchantInfo.name : '- -' }}
        </a-col>
        <a-col :span="6"><b>地 址：</b>
          {{ merchantInfo.address ? merchantInfo.address : '- -' }}
        </a-col>
        <a-col :span="6"><b>负责人：</b>
          {{ merchantInfo.principal }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="6"><b>联系方式：</b>
          {{ merchantInfo.phone }}
        </a-col>
        <a-col :span="6"><b>菜系：</b>
          {{ merchantInfo.dishes ? merchantInfo.dishes : '- -' }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="orderItemInfo.length !== 0">
      <a-row style="padding-left: 24px;padding-right: 24px;" :gutter="15">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">购买菜品</span></a-col>
        <a-col :span="18">
          <a-table :columns="columns" :data-source="orderItemInfo"></a-table>
        </a-col>
        <a-col :span="6">
          <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">选择配送员</span></a-col>
          <a-select v-model="staffId" style="width: 100%;">
            <a-select-option v-for="(item, index) in staffList" :value="item.id" :key="index">{{ item.name }}</a-select-option>
          </a-select>
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="addressInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">收货地址</span></a-col>
        <a-col :span="6"><b>收货编号：</b>
          {{ addressInfo.code }}
        </a-col>
        <a-col :span="6"><b>详细地址：</b>
          {{ addressInfo.address ? addressInfo.address : '- -' }}
        </a-col>
        <a-col :span="6"><b>联系人：</b>
          {{ addressInfo.contactPerson ? addressInfo.contactPerson : '- -' }}
        </a-col>
        <a-col :span="6"><b>联系方式：</b>
          {{ addressInfo.contactMethod }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="staffInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">配送员信息</span></a-col>
        <a-col :span="6"><b>员工姓名：</b>
          {{ staffInfo.name }}
        </a-col>
        <a-col :span="6"><b>性别：</b>
          <span v-if="orderInfo.type === '1'">男</span>
          <span v-if="orderInfo.type === '2'">女</span>
        </a-col>
        <a-col :span="6"><b>员工工号：</b>
          {{ staffInfo.code }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
  </a-modal>
</template>

<script>
import moment from 'moment'
import {mapState} from 'vuex'
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
moment.locale('zh-cn')
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'OrderAudit',
  props: {
    orderShow: {
      type: Boolean,
      default: false
    },
    orderData: {
      type: Object
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.orderShow
      },
      set: function () {
      }
    },
    columns () {
      return [{
        title: '菜品名称',
        dataIndex: 'dishesName'
      }, {
        title: '图片',
        dataIndex: 'images',
        customRender: (text, record, index) => {
          if (!record.images) return <a-avatar shape="square" icon="user" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
            </template>
            <a-avatar shape="square" icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
          </a-popover>
        }
      }, {
        title: '购买数量',
        dataIndex: 'amount'
      }, {
        title: '单价',
        dataIndex: 'unitPrice'
      }, {
        title: '总价格',
        dataIndex: 'totalPrice',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }]
    }
  },
  watch: {
    orderShow: function (value) {
      if (value) {
        this.dataInit(this.orderData.id)
      }
    }
  },
  data () {
    return {
      formItemLayout,
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      repairInfo: null,
      reserveInfo: null,
      durgList: [],
      logisticsList: [],
      current: 0,
      userInfo: null,
      orderInfo: null,
      merchantInfo: null,
      orderItemInfo: [],
      addressInfo: null,
      staffInfo: null,
      evaluateInfo: null,
      staffId: null,
      staffList: []
    }
  },
  mounted () {
    this.selectStaffList()
  },
  methods: {
    moment,
    dataInit (orderId) {
      this.$get(`/cos/order-info/${orderId}`).then((r) => {
        this.userInfo = r.data.user
        this.orderInfo = r.data.order
        this.merchantInfo = r.data.merchant
        this.orderItemInfo = r.data.orderItem
        this.addressInfo = r.data.address
        this.staffInfo = r.data.staff
        this.evaluateInfo = r.data.evaluate
        this.imagesInit(this.merchantInfo.images)
      })
    },
    selectStaffList () {
      this.$get(`/cos/staff-info/selectStaffByMerchant/${this.currentUser.userId}`).then((r) => {
        this.staffList = r.data.data
      })
    },
    checkDealer () {
      if (this.staffId === null) {
        this.$message.warn('请选择配送员工')
        return false
      }
      this.$get(`/cos/order-info/checkDealer`, {orderCode: this.orderInfo.code, staffId: this.staffId}).then((r) => {
        this.$emit('success')
      })
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
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    submit () {
      console.log(this.takeShop)
      console.log(this.returnShop)
      if (this.takeShop !== '' && this.returnShop !== '') {
        this.$put(`/cos/order-info`, {
          'takeShop': this.takeShop,
          'returnShop': this.returnShop,
          'id': this.orderInfo.id
        }).then((r) => {
          this.$emit('success')
        })
      } else {
        this.$message.warn('请选择车店')
      }
    },
    onClose () {
      this.$emit('close')
    },
    cleanData () {
      this.staffCheck = []
      this.driverCheck = []
    }
  }
}
</script>

<style scoped>

</style>
