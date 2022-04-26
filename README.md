# 地图封装

## 前言

这个开源示例Demo主要演示了 传入目的节点的经纬度绘制步行导航路线

## 接入步骤

### 初始化sdk

#### 添加module依赖：

 ```
 implementation project(':bdmap')
  ```

#### 添加libs依赖：

 ```
 android{
     repositories {
         flatDir {
             dirs project(':bdmap').file('libs')
         }
     }
  }
   ```

- 项目的application继承MapApplication完成初始化

### 功能

#### 1.启动步行路径规划页面并传值

   ```
  ActivityHelper.startWalkListActivity(MainActivity.this,IfLyMapData);

   ```

#### 2.启动定位功能，回调经纬度等信息

      ```
       BDLocationUtil.getInstance().startLocation(this, new BDLocationUtil.LocationListener() {
                @Override
                public void onLocation(BDLocation location) {
                    Log.d("TAG", location.getCity() + "latu:" + location.getLatitude());

                }

                @Override
                public void onLocationFailed() {

                }
            });

      ```

### 编译运行

按步骤集成后，连上Android设备，编译并运行。
