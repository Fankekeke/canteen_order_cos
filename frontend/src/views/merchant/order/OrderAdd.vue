<template>
  <a-drawer
    title="支付"
    :maskClosable="false"
    width=1350
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="orderAddShow"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form" layout="vertical" v-if="nextFlag == 1">
      <a-row :gutter="10">
        <div style="font-size: 13px;font-family: SimHei" v-if="orderInfo !== null">
          <a-row style="padding-left: 24px;padding-right: 24px;">
            <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">订单信息</span></a-col>
            <a-col :span="6"><b>订单编号：</b>
              {{ orderInfo.code }}
            </a-col>
            <a-col :span="6"><b>订单积分：</b>
              {{ orderInfo.integral }}
            </a-col>
            <a-col :span="6"><b>订单状态：</b>
              <span v-if="orderInfo.status === '0'" style="color: red">未支付</span>
              <span v-if="orderInfo.status === '1'" style="color: blue">已支付</span>
              <span v-if="orderInfo.status === '2'" style="color: orange">配送中</span>
              <span v-if="orderInfo.status === '3'" style="color: green">已收货</span>
            </a-col>
            <a-col :span="6"><b>下单时间：</b>
              {{ orderInfo.createDate }}
            </a-col>
          </a-row>
          <br/>
          <a-row style="padding-left: 24px;padding-right: 24px;">
            <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">选择 外送/堂食</span></a-col>
            <a-col :span="24">
              <a-radio-group button-style="solid" v-model="orderInfo.type">
                <a-radio-button value="0">
                  堂食
                </a-radio-button>
                <a-radio-button value="1">
                  外送
                </a-radio-button>
              </a-radio-group>
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
            <a-col :span="6"><b>订单价格：</b>
              {{ orderInfo.orderPrice ? orderInfo.orderPrice + '元' : '- -' }}
            </a-col>
            <a-col :span="6"><b>折后价格：</b>
              {{ orderInfo.afterOrderPrice ? orderInfo.afterOrderPrice + '元' : '- -' }}
            </a-col>
            <br/>
            <br/>
            <a-col :span="6"><b>会员折扣：</b>
              {{ orderInfo.discount }} 元
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
        </div>
        <br/>
        <div style="font-size: 13px;font-family: SimHei" v-if="orderItemInfo.length !== 0">
          <a-row style="padding-left: 24px;padding-right: 24px;">
            <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">购买菜品</span></a-col>
            <a-table :columns="columns" :data-source="orderItemInfo"></a-table>
          </a-row>
          <br/>
        </div>
        <a-col :span="16">
          <a-form-item label='备注'>
            <a-textarea v-decorator="[
            'remark'
            ]" :rows="4"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
    <div v-if="nextFlag == 2" style="font-size: 13px;font-family: SimHei">
      <a-row :gutter="10">
        <a-divider orientation="left">
          <span style="font-size: 13px">价格规则</span>
        </a-divider>
        <a-col :span="24">
          【基础金额 + (距离 * 距离单价) + 配送车辆金额 + (配送员数量 * 配送员金额) + 无电梯费用】
        </a-col>
      </a-row>
      <br/>
      <a-row :gutter="10">
        <a-col :span="6"><b>基础金额：</b>
           元
        </a-col>
        <a-col :span="6"><b>距离：</b>
          千米
        </a-col>
        <a-col :span="6"><b>距离单价：</b>
          元
        </a-col>
      </a-row>
      <br/>
      <a-row :gutter="10">
        <a-col :span="6"><b>配送车辆金额：</b>
          元
        </a-col>
        <a-col :span="6"><b>配送员数量：</b>
          个
        </a-col>
        <a-col :span="6"><b>配送员金额：</b>
          元
        </a-col>
        <a-col :span="6"><b>无电梯费用：</b>
          元
        </a-col>
      </a-row>
      <br/>
      <br/>
      <div style="text-align: center;margin-top: 180px">
        <a-icon type="smile" theme="twoTone" style="font-size: 130px"/>
        <p style="margin-top: 20px;font-size: 28px">您需要缴纳50元</p>
      </div>
    </div>
    <drawerMap :childrenDrawerShow="childrenDrawer" @handlerClosed="handlerClosed"></drawerMap>
    <div class="drawer-bootom-button">
      <a-popconfirm title="确定放弃编辑？" @confirm="onClose" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="next" type="primary" v-if="nextFlag == 1">下一步</a-button>
      <a-button @click="pay" type="primary" v-if="nextFlag == 2">支付</a-button>
    </div>
  </a-drawer>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
import drawerMap from '@/utils/map/searchmap/drawerMap'
import {mapState} from 'vuex'

function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}

const formItemLayout = {
  labelCol: {span: 24},
  wrapperCol: {span: 24}
}
export default {
  name: 'inventoryAdd',
  props: {
    orderAddShow: {
      default: false
    }
  },
  components: {
    drawerMap
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.orderAddShow
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
    'orderAddShow': function (value) {
      if (value) {
        this.dataInit(this.orderData.id)
      }
    }
  },
  data () {
    return {
      payLoading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      localPoint: {},
      stayAddress: '',
      childrenDrawer: false,
      flagType: 0,
      nextFlag: 1,
      userInfo: null,
      orderInfo: null,
      merchantInfo: null,
      orderItemInfo: [],
      addressInfo: null,
      addressList: []
    }
  },
  mounted () {
    this.selectAddress()
  },
  methods: {
    selectAddress () {
      this.$get(`/cos/address-info/listByUserId/${this.currentUser.userId}`).then((r) => {
        this.addressList = r.data.data
      })
    },
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
        this.payLoading = false
      })
    },
    changeAddress (addressId) {
      if (!addressId) {
        return false
      }
      this.payLoading = true
      let orderInfo = this.orderInfo
      orderInfo.orderItemList = this.orderItemInfo
      orderInfo.type = '1'
      orderInfo.addressId = addressId
      this.$put(`/cos/order-info`, ...orderInfo).then((r) => {
        this.dataInit(this.orderData.id)
      })
    },
    next () {
      this.nextFlag = 2
    },
    handleChange (value, record) {
      console.log(value)
      if (value) {
        this.drugList.forEach(e => {
          if (e.drugId === value) {
            record.brand = e.brand
            record.classification = e.classification
            record.dosageForm = e.dosageForm
            record.unitPrice = e.unitPrice
            record.reserve = e.reserve
            record.drugId = e.drugId
            console.log(record)
          }
        })
      }
    },
    handlerClosed (localPoint) {
      this.childrenDrawer = false
      if (localPoint !== null && localPoint !== undefined) {
        this.localPoint = localPoint
        console.log(this.localPoint)
        let address = baiduMap.getAddress(localPoint)
        address.getLocation(localPoint, (rs) => {
          if (rs != null) {
            if (rs.address !== undefined && rs.address.length !== 0) {
              this.stayAddress = rs.address
            }
            if (rs.surroundingPois !== undefined) {
              if (rs.surroundingPois.address !== undefined && rs.surroundingPois.address.length !== 0) {
                this.stayAddress = rs.surroundingPois.address
              }
            }
            let obj = {}
            console.log(this.flagType)
            if (this.flagType === 1) {
              obj['startAddress'] = this.stayAddress
              obj['startLongitude'] = localPoint.lng
              obj['startLatitude'] = localPoint.lat
            } else {
              obj['endAddress'] = this.stayAddress
              obj['endLongitude'] = localPoint.lng
              obj['endLatitude'] = localPoint.lat
            }
            this.form.setFieldsValue(obj)
          }
        })
      }
    },
    showChildrenDrawer (value) {
      this.flagType = value
      this.childrenDrawer = true
    },
    onChildrenDrawerClos () {
      this.childrenDrawer = false
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
    picHandleChange ({fileList}) {
      this.fileList = fileList
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        values.orderDetailList = JSON.stringify(this.dataList)
        if (!err) {
          this.loading = true
          this.$post('/cos/order-info/platform', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
