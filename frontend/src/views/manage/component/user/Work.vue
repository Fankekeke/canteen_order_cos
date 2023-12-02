<template>
  <div style="background:#ECECEC; padding:30px;margin-top: 30px;margin-bottom: 30px">
    <div style="height: 450px;">
      <div style="height: 350px;background-image: url(../static/img/house.jpg);padding: 50px">
        <div style="font-size: 35px;font-weight: 500;color: white;font-family: SimHei">你好 朋友</div>
        <div style="font-size: 22px;font-weight: 500;color: white;font-family: SimHei">开始点餐</div>
        <div style="height: 180px;margin-top: 100px">
          <a-card :bordered="false" hoverable style="height: 100%;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);color:#fff">
            <a-row style="padding: 50px;margin: 0 auto">
              <a-col :span="16">
                <a-row>
                  <a-col :span="18">
                    <a-input v-model="key" placeholder="开始点餐"/>
                  </a-col>
                  <a-col :span="4" :offset="2">
                    <a-button type="primary" @click="fetch">
                      查找
                    </a-button>
                  </a-col>
                  <a-col :span="24"></a-col>
                  <a-col :span="24" style="font-size: 15px;font-family: SimHei">
                    <div style="margin-top: 30px">
                      <a style="margin-right: 15px">川菜</a>
                      <a style="margin-right: 15px">湘菜</a>
                      <a style="margin-right: 15px">粤菜</a>
                      <a style="margin-right: 15px">快餐</a>
                      <a style="margin-right: 15px">西餐</a>
                      <a style="margin-right: 15px">中餐</a>
                    </div>
                  </a-col>
                </a-row>
              </a-col>
              <a-col :span="6" :offset="2">
              </a-col>
            </a-row>
          </a-card>
        </div>
      </div>
    </div>
    <a-row :gutter="30" style="padding: 35px;margin: 0 auto">
      <a-col :span="6" v-for="(item, index) in roomList" :key="index">
        <div style="background: #e8e8e8">
          <a-carousel autoplay style="height: 200px;" v-if="item.images !== undefined && item.images !== ''">
            <div style="width: 100%;height: 150px" v-for="(item, index) in item.images.split(',')" :key="index">
              <img :src="'http://127.0.0.1:9527/imagesWeb/'+item" style="width: 100%;height: 200px">
            </div>
          </a-carousel>
          <a-card :bordered="false">
            <div slot="title">
              <div style="font-size: 14px;font-family: SimHei">
                <div>
                  <a-badge status="success" v-if="item.currentStatus === '1'" style="display: contents;margin: 0 auto"/>
                  <a-badge status="error" v-if="item.currentStatus === '0'" style="display: contents;margin: 0 auto"/>
                  {{ item.name }}  <a-tag color="green" style="font-size: 11px">{{ item.dishes }}</a-tag>
                </div>
                <div style="font-size: 12px;margin-top: 4px">
                  <a-icon type="environment" />  {{ item.address }}
                </div>
              </div>
            </div>
            <template slot="actions" class="ant-card-actions">
              <a-icon key="shopping" type="shopping" @click="view(item)"/>
            </template>
          </a-card>
        </div>
      </a-col>
    </a-row>
    <Map :orderData="orderMapView.merchantInfo"
         @close="handleorderMapViewClose"
         :orderShow="orderMapView.visiable">
    </Map>
  </div>
</template>

<script>

import {mapState} from 'vuex'
import VehicleView from './VehicleView.vue'
import Map from './Map.vue'
export default {
  name: 'Work',
  components: {Map, VehicleView},
  data () {
    return {
      orderMapView: {
        merchantInfo: null,
        visiable: false
      },
      key: '',
      roomList: [],
      roomTypeList: [],
      loading: false,
      vehicleView: {
        visiable: false,
        data: null
      },
      rentView: {
        visiable: false,
        data: null
      },
      startDate: null,
      endDate: null
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  mounted () {
    this.getWorkStatusList()
  },
  methods: {
    handlevehicleViewClose () {
      this.vehicleView.visiable = false
    },
    handlevehicleViewSuccess () {
      this.vehicleView.visiable = false
      this.$message.success('添加订单成功')
    },
    orderSuccess () {
      this.rentView.visiable = false
      this.$message.success('添加订单成功')
      this.fetch()
    },
    collectDelete (id) {
      this.$delete(`/cos/collect-info/${id}`).then((r) => {
        this.$message.success('取消收藏成功')
        this.fetch()
      })
    },
    collectAdd (roomCode) {
      let data = { userId: this.currentUser.userId, roomCode }
      this.$post(`/cos/collect-info`, data).then((r) => {
        this.$message.success('成功')
        this.fetch()
      })
    },
    view (record) {
      // if (record.currentStatus === '0') {
      //   this.$message.warn('此商家不在营业时间内')
      //   return false
      // }
      this.orderMapView.merchantInfo = record
      this.orderMapView.visiable = true
    },
    handleorderMapViewClose () {
      this.orderMapView.visiable = false
    },
    getRoomType () {
      this.$get(`/cos/vehicle-type-info/list`).then((r) => {
        this.roomTypeList = r.data.data
      })
    },
    getWorkStatusList () {
      this.$get(`/cos/order-info/selectMerchantList`, { key: this.key }).then((r) => {
        this.roomList = r.data.data
      })
    },
    fetch () {
      this.getWorkStatusList()
    }
  }
}
</script>

<style scoped>
>>> .ant-card-meta-title {
  font-size: 13px;
  font-family: SimHei;
}
>>> .ant-card-meta-description {
  font-size: 12px;
  font-family: SimHei;
}
>>> .ant-divider-with-text-left {
  margin: 0;
}

>>> .ant-card-head-title {
  font-size: 13px;
  font-family: SimHei;
}
>>> .ant-card-extra {
  font-size: 13px;
  font-family: SimHei;
}
.ant-carousel >>> .slick-slide {
  text-align: center;
  height: 250px;
  line-height: 250px;
  overflow: hidden;
}

</style>
