<template>
  <a-modal v-model="show" title="菜品详情" @cancel="onClose" :width="1000">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="dishesData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>菜品编号：</b>
          {{ dishesData.code }}
        </a-col>
        <a-col :span="8"><b>菜品名称：</b>
          {{ dishesData.name ? dishesData.name : '- -' }}
        </a-col>
        <a-col :span="8"><b>原料：</b>
          {{ dishesData.rawMaterial ? dishesData.rawMaterial : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>份量：</b>
          {{ dishesData.portion }}
        </a-col>
        <a-col :span="8"><b>口味：</b>
          {{ dishesData.taste }}
        </a-col>
        <a-col :span="8"><b>价格：</b>
          {{ dishesData.unitPrice }} 元
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>销量：</b>
          {{ dishesData.saleNum }}
        </a-col>
        <a-col :span="8"><b>上架状态：</b>
          <span v-if="dishesData.status == 0" style="color: red">下架</span>
          <span v-if="dishesData.status == 1" style="color: green">上架</span>
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ dishesData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>热量：</b>
          {{ dishesData.heat }}
        </a-col>
        <a-col :span="8"><b>蛋白质：</b>
          {{ dishesData.protein }}
        </a-col>
        <a-col :span="8"><b>脂肪：</b>
          {{ dishesData.fat }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>所属商家：</b>
          {{ dishesData.merchantName }}
        </a-col>
        <a-col :span="8"><b>联系人：</b>
          {{ dishesData.principal }}
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          {{ dishesData.phone }}
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
  name: 'dishesView',
  props: {
    dishesShow: {
      type: Boolean,
      default: false
    },
    dishesData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.dishesShow
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
    dishesShow: function (value) {
      if (value) {
        this.imagesInit(this.dishesData.images)
      }
    }
  },
  methods: {
    local (dishesData) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(dishesData.longitude, dishesData.latitude)
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
