# InstaStack SDK

[![](https://jitpack.io/v/shreenivas-ch/TrollaHealth.svg)](https://jitpack.io/#shreenivas-ch/TrollaHealth)

## Min SDK Version - 21

InstaStack SDK - Integrate our sdk and earn commissions on orders placed through your application


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
