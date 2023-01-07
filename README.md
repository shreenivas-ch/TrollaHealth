# InstaStack SDK

[![](https://jitpack.io/v/shreenivas-ch/TrollaHealth.svg)](https://jitpack.io/#shreenivas-ch/TrollaHealth)

## Min SDK Version - 21

InstaStack SDK - Integrate our sdk and earn commissions on orders placed through your application

<p align="center">
<img src="https://user-images.githubusercontent.com/1378730/37100456-9225c0c6-2255-11e8-972c-e365ef2659fa.png" alt="alt text" width="200" hspace="40">
</p>


## Add library to your project

```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

```gradle
dependencies {
	implementation 'com.github.shreenivas-ch:TrollaHealth:1.1.0'
}
```

## How to initiate SDK
```kotlin
findViewById<TextView>(R.id.yourButton).setOnClickListener {
             TrollaHealthManager.Builder().appid("YourAppID").context(this).application(application)
                .build()
                .launch()
        }
```
