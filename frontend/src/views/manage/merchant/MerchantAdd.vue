<template>
  <a-drawer
    title="新增商家"
    :maskClosable="false"
    width=1000
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="show"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label='商家名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入商家名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='负责人' v-bind="formItemLayout">
            <a-input v-decorator="[
            'principal',
            { rules: [{ required: true, message: '请输入负责人!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='店铺地址'>
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
          <a-form-item label='联系方式' v-bind="formItemLayout">
            <a-input v-decorator="[
            'phone',
            { rules: [{ required: true, message: '请输入联系方式!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='菜系' v-bind="formItemLayout">
            <a-select v-decorator="[
            'dishes',
            { rules: [{ required: true, message: '请输入菜系!' }] }
            ]">
              <a-select-option value="川菜">川菜</a-select-option>
              <a-select-option value="湘菜">湘菜</a-select-option>
              <a-select-option value="粤菜">粤菜</a-select-option>
              <a-select-option value="快餐">快餐</a-select-option>
              <a-select-option value="西餐">西餐</a-select-option>
              <a-select-option value="中餐">中餐</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='营业星期' v-bind="formItemLayout">
            <div :style="{ borderBottom: '1px solid #E9E9E9' }">
              <a-checkbox :indeterminate="indeterminate" :checked="checkAll" @change="onCheckAllChange">
                Check all
              </a-checkbox>
            </div>
            <br />
            <a-checkbox-group v-model="checkedList" :options="plainOptions" @change="onChange" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='开始营业时间' v-bind="formItemLayout">
            <a-time-picker :default-open-value="moment('00:00:00', 'HH:mm:ss')" style="width: 100%" v-decorator="[
            'operateStartTime',
            { rules: [{ required: true, message: '请输入开始营业时间!' }] }
            ]" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='营业结束时间' v-bind="formItemLayout">
            <a-time-picker style="width: 100%" v-decorator="[
            'operateEndTime',
            { rules: [{ required: true, message: '请输入营业结束时间!' }] }
            ]" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='店铺介绍' v-bind="formItemLayout">
            <a-textarea :rows="6" v-decorator="[
            'content',
             { rules: [{ required: true, message: '请输入店铺介绍!' }] }
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
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
const plainOptions = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
const defaultCheckedList = ['周一', '周二', '周三', '周四', '周五']
export default {
  name: 'merchantAdd',
  props: {
    merchantAddVisiable: {
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
        return this.merchantAddVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      checkedList: defaultCheckedList,
      indeterminate: true,
      checkAll: false,
      plainOptions,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      localPoint: {},
      staymerchant: '',
      childrenDrawer: false
    }
  },
  methods: {
    moment,
    onChange (checkedList) {
      this.indeterminate = !!checkedList.length && checkedList.length < plainOptions.length
      this.checkAll = checkedList.length === plainOptions.length
      console.log(this.checkedList)
    },
    onCheckAllChange (e) {
      Object.assign(this, {
        checkedList: e.target.checked ? plainOptions : [],
        indeterminate: false,
        checkAll: e.target.checked
      })
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
      if (this.checkedList.length === 0) {
        this.$message.warn('至少选择一天')
        return false
      }
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        images.push(image.response)
      })
      this.form.validateFields((err, values) => {
        values.userId = this.currentUser.userId
        values.images = images.length > 0 ? images.join(',') : null
        values.operateDay = this.checkedList.join(',')
        values.operateStartTime = moment(values.operateStartTime).format('HH:mm:ss')
        values.operateEndTime = moment(values.operateEndTime).format('HH:mm:ss')
        if (!err) {
          this.loading = true
          this.$post('/cos/merchant-info', {
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
