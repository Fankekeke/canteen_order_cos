<template>
  <a-modal v-model="show" title="修改价格规则" @cancel="onClose" :width="480">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        修改
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="24">
          <a-form-item label='规则内容' v-bind="formItemLayout">
            <a-input v-decorator="[
            'remark',
            { rules: [{ required: true, message: '请输入规则内容!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='单价' v-bind="formItemLayout">
            <a-input-number style="width: 100%;" v-decorator="[
            'unitPrice',
            { rules: [{ required: true, message: '请输入单价!' }] }
            ]" :min="1" :step="1"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='最小值' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
                              'minValue',
                              { rules: [{ required: true, message: '请输入最小值!' }] }
                              ]" :min="0" :step="0.1"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='最大值' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
                              'maxValue',
                              { rules: [{ required: true, message: '请输入最小值!' }] }
                              ]" :min="0" :step="0.1"/>
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
  name: 'rulesEdit',
  props: {
    rulesEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.rulesEditVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      rowId: null,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  methods: {
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
    setFormValues ({...rules}) {
      this.rowId = rules.id
      let fields = ['remark', 'unitPrice', 'type', 'minValue', 'maxValue']
      let obj = {}
      Object.keys(rules).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = rules[key]
        }
      })
      this.form.setFieldsValue(obj)
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
        values.id = this.rowId
        if (!err) {
          this.loading = true
          this.$put('/cos/price-rules', {
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

<style scoped>/* 表单项标签样式 */
.ant-form-item-label {
  padding-bottom: 8px;
}

/* 输入框聚焦效果 */
.ant-input:focus,
.ant-input-number-input:focus {
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
  border-color: #1890ff;
}

/* 表单项间距调整 */
.ant-form-item {
  margin-bottom: 20px;
}

/* 最后一个表单项减少底部边距 */
.ant-form-item:last-child {
  margin-bottom: 10px;
}

/* 数字输入框微调 */
.ant-input-number {
  width: 100%;
}
</style>
