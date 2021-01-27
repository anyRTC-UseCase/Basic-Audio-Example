//
//  UserModel.h
//  AR-iOS-Voice-Base
//
//  Created by anyRTC on 2021/1/25.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN


@interface UserModel : NSObject

/// 下行
@property (nonatomic ,assign) NSInteger rxAudioKBitrate;

/// 上行
@property (nonatomic ,assign) NSInteger txAudioKBitrate;

@property (nonatomic ,copy) NSString *uid;


@end

NS_ASSUME_NONNULL_END
