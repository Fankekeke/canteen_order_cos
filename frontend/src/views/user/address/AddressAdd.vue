<template>
  <a-drawer
    title="新增用户地址"
    :maskClosable="false"
    width=800
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="show"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label='联系人' v-bind="formItemLayout">
            <a-input v-decorator="[
            'contactPerson',
            { rules: [{ required: true, message: '请输入联系人!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='联系方式' v-bind="formItemLayout">
            <a-input v-decorator="[
            'contactMethod',
            { rules: [{ required: true, message: '请输入联系方式!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='用户详细地址'>
            <a-input-search
              v-decorator="[
              'address'
              ]"
              enter-button="选择"
              @search="showChildrenDrawer"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='经度' v-bind="formItemLayout">
            <a-input v-decorator="[
            'longitude',
            { rules: [{ required: true, message: '请输入经度!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='纬度' v-bind="formItemLayout">
            <a-input v-decorator="[
            'latitude',
            { rules: [{ required: true, message: '请输入纬度!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='省份' v-bind="formItemLayout">
            <a-input v-decorator="[
            'province',
            { rules: [{ required: true, message: '请输入省份!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='市区' v-bind="formItemLayout">
            <a-input v-decorator="[
            'city',
            { rules: [{ required: true, message: '请输入市区!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='区' v-bind="formItemLayout">
            <a-input v-decorator="[
            'area',
            { rules: [{ required: true, message: '请输入区!' }] }
            ]"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
    <drawerMap :childrenDrawerShow="childrenDrawer" @handlerClosed="handlerClosed"></drawerMap>
    <div class="drawer-bootom-button">
      <a-popconfirm title="确定放弃编辑？" @confirm="onClose" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        提交
      </a-button>
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
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'addressAdd',
  props: {
    addressAddVisiable: {
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
        return this.addressAddVisiable
      },
      set: function () {
      }
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
      localPoint: {},
      stayAddress: '',
      childrenDrawer: false
    }
  },
  methods: {
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
            obj['address'] = this.stayAddress
            obj['longitude'] = localPoint.lng
            obj['latitude'] = localPoint.lat
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
      this.form.validateFields((err, values) => {
        values.userId = this.currentUser.userId
        if (!err) {
          this.loading = true
          this.$post('/cos/address-info', {
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
