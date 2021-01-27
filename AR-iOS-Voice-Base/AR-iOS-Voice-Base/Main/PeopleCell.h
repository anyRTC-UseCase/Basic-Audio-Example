//
//  PeopleCell.h
//  AR-iOS-Voice-Base
//
//  Created by anyRTC on 2021/1/25.
//

#import <UIKit/UIKit.h>
#import "UserModel.h"
NS_ASSUME_NONNULL_BEGIN

@interface PeopleCell : UITableViewCell

+ (instancetype)createPeopleCellWithTableView:(UITableView *)tableView IndexPath:(NSIndexPath *)indexPath;

@property (nonatomic ,strong) UserModel *model;

@end

NS_ASSUME_NONNULL_END
