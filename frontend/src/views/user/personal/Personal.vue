<template>
  <a-row :gutter="20">
    <a-col :span="6">
      <a-card :loading="loading" :bordered="false">
        <a-form :form="form" layout="vertical">
          <div v-if="this.expertInfo != null">
            <a-form-item label='我的额度'>
              <h3 style="display: inline;font-size: 18px">{{ this.expertInfo.quota }}</h3>元
              <a-button type="primary" @click="showModal" size="small" style="margin-left: 50px">充值</a-button>
            </a-form-item>
          </div>
          <a-row :gutter="20">
            <a-col :span="12">
              <a-form-item label='用户编号' v-bind="formItemLayout">
                <a-input disabled v-decorator="[
                'code',
                ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label='注册时间' v-bind="formItemLayout">
                <a-input disabled v-decorator="[
                'createDate',
                ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label='用户名称' v-bind="formItemLayout">
                <a-input v-decorator="[
                'name',
                { rules: [{ required: true, message: '请输入用户名称!' }] }
                ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label='邮箱地址' v-bind="formItemLayout">
                <a-input v-decorator="[
                'mail'
                ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label='联系方式' v-bind="formItemLayout">
                <a-input v-decorator="[
                'phone'
                ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label='省份' v-bind="formItemLayout">
                <a-input v-decorator="[
                'province'
                ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label='市' v-bind="formItemLayout">
                <a-input v-decorator="[
                'city'
                ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label='区' v-bind="formItemLayout">
                <a-input v-decorator="[
                'area'
                ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label='联系地址' v-bind="formItemLayout">
                <a-input v-decorator="[
                'address'
                ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label='头像' v-bind="formItemLayout">
                <a-upload
                  name="avatar"
                  action="http://127.0.0.1:9527/file/fileUpload/"
                  list-type="picture-card"
                  :file-list="fileList"
                  @preview="handlePreview"
                  @change="picHandleChange"
                >
                  <div v-if="fileList.length < 1">
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
        <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
          修改
        </a-button>
      </a-card>
    </a-col>
    <a-col :span="18">
      <div style="background:#ECECEC; padding:30px;margin-top: 30px">
        <a-card :bordered="false">
          <a-spin :spinning="dataLoading">
            <a-calendar>
              <ul slot="dateCellRender" slot-scope="value" class="events">
                <li v-for="item in getListData(value)" :key="item.content">
                  <a-badge :status="item.type" :text="item.content" />
                </li>
              </ul>
            </a-calendar>
          </a-spin>
        </a-card>
      </div>
    </a-col>
    <a-modal
      title="额度充值"
      :visible="visible"
      :confirm-loading="confirmLoading"
      :width="600"
      @ok="auditQuota"
      @cancel="handleCancel1"
    >
      <a-radio-group v-model="quota" button-style="solid">
        <a-radio-button value="50">
          50元
        </a-radio-button>
        <a-radio-button value="100">
          100元
        </a-radio-button>
        <a-radio-button value="200">
          200元
        </a-radio-button>
        <a-radio-button value="500">
          500元
        </a-radio-button>
      </a-radio-group>
    </a-modal>
  </a-row>
</template>

<script>
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
export default {
  name: 'User',
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  data () {
    return {
      visible: false,
      confirmLoading: false,
      form: this.$form.createForm(this),
      formItemLayout,
      loading: false,
      courseInfo: [],
      dataLoading: false,
      fileList: [],
      previewVisible: false,
      quota: 50,
      previewImage: '',
      expertInfo: null
    }
  },
  mounted () {
    this.getExpertInfo(this.currentUser.userId)
  },
  methods: {
    showModal () {
      this.visible = true
    },
    handleCancel1 (e) {
      console.log('Clicked cancel button')
      this.visible = false
    },
    auditQuota () {
      this.$post('/cos/pay/saveUserQuota', { userId: this.currentUser.userId, quota: this.quota, type: 1 }).then((r) => {
        const divForm = document.getElementsByTagName('div')
        if (divForm.length) {
          document.body.removeChild(divForm[0])
        }
        const div = document.createElement('div')
        div.innerHTML = r.data.msg // data就是接口返回的form 表单字符串
        // console.log(div.innerHTML)
        document.body.appendChild(div)
        document.forms[0].setAttribute('target', '_self') // 新开窗口跳转
        document.forms[0].submit()
      })
    },
    isDuringDate (beginDateStr, endDateStr, curDataStr) {
      let curDate = new Date(curDataStr)
      let beginDate = new Date(beginDateStr)
      let endDate = new Date(endDateStr)
      if (curDate >= beginDate && curDate <= endDate) {
        return true
      }
      return false
    },
    getListData (value) {
      let listData = []
      this.courseInfo.forEach(item => {
        if ((moment(value).format('YYYY-MM-DD')) === (moment(item.createDate).format('YYYY-MM-DD'))) {
          listData.push({type: 'success', content: item.code})
        }
      })
      return listData || []
    },
    getExpertInfo (userId) {
      this.dataLoading = true
      this.$get(`/cos/user-info/user/${userId}`).then((r) => {
        this.expertInfo = r.data.user
        this.setFormValues(this.expertInfo)
        this.courseInfo = r.data.order
        this.dataLoading = false
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
    setFormValues ({...expert}) {
      this.rowId = expert.id
      let fields = ['code', 'name', 'phone', 'province', 'city', 'area', 'mail', 'images', 'address', 'createDate']
      let obj = {}
      Object.keys(expert).forEach((key) => {
        if (key === 'images') {
          this.fileList = []
          this.imagesInit(expert['images'])
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = expert[key]
        }
      })
      this.form.setFieldsValue(obj)
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        if (image.response !== undefined) {
          images.push(image.response)
        } else {
          images.push(image.name)
        }
      })
      this.form.validateFields((err, values) => {
        values.id = this.rowId
        values.images = images.length > 0 ? images.join(',') : null
        if (!err) {
          this.loading = true
          this.$put('/cos/user-info', {
            ...values
          }).then((r) => {
            this.$message.success('更新成功')
            this.loading = false
            this.getExpertInfo(this.currentUser.userId)
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
