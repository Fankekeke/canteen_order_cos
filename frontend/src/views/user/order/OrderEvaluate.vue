<template>
  <a-modal v-model="show" title="订单评价" @cancel="onClose" :width="1000">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        提交
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
    <div style="font-size: 13px;font-family: SimHei" v-if="orderItemInfo.length !== 0">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">购买菜品</span></a-col>
        <a-table :columns="columns" :data-source="orderItemInfo" :pagination="false"></a-table>
      </a-row>
      <br/>
    </div>
    <a-form :form="form" layout="vertical" style="padding: 20px">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label='评价分数' v-bind="formItemLayout">
            <a-rate v-decorator="[
            'score',
            { rules: [{ required: true, message: '请输入评价分数!' }] }
            ]" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='评价内容' v-bind="formItemLayout">
            <a-textarea :rows="6" v-decorator="[
            'content',
             { rules: [{ required: true, message: '请输入评价内容!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='图册' v-bind="formItemLayout">
            <a-upload
              name="avatar"
              action="http://127.0.0.1:9527/file/fileUpload/"
              list-type="picture-card"
              :file-list="fileList"
              @preview="handlePreview"
              @change="picHandleChange"
            >
              <div v-if="fileList.length < 8">
                <a-icon type="plus" />
                <div class="ant-upload-text">
                  Upload
                </div>
              </div>
            </a-upload>
            <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
              <img alt="example" style="width: 100%" :src="previewImage" />
            </a-modal>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
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
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'evaluateAdd',
  props: {
    evaluateAddVisiable: {
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
        return this.evaluateAddVisiable
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
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      durgList: [],
      logisticsList: [],
      current: 0,
      buyer: null,
      sale: null,
      commodityData: null,
      userInfo: null,
      orderInfo: null,
      merchantInfo: null,
      orderItemInfo: [],
      addressInfo: null,
      staffInfo: null,
      evaluateInfo: null
    }
  },
  watch: {
    evaluateAddVisiable: function (value) {
      if (value) {
        this.fileList = []
        this.dataInit(this.orderData.id)
      }
    }
  },
  methods: {
    dataInit (orderId) {
      this.$get(`/cos/order-info/${orderId}`).then((r) => {
        this.userInfo = r.data.user
        this.orderInfo = r.data.order
        this.merchantInfo = r.data.merchant
        this.orderItemInfo = r.data.orderItem
        this.addressInfo = r.data.address
        this.staffInfo = r.data.staff
        this.evaluateInfo = r.data.evaluate
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
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        images.push(image.response)
      })
      this.form.validateFields((err, values) => {
        values.orderId = this.orderData.id
        values.userId = this.orderData.userId
        values.merchantId = this.orderData.merchantId
        values.images = images.length > 0 ? images.join(',') : null
        if (!err) {
          this.loading = true
          this.$post('/cos/evaluate-info', {
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
