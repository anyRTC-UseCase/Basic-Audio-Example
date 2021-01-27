//
//  PeopleCell.m
//  AR-iOS-Voice-Base
//
//  Created by anyRTC on 2021/1/25.
//

#import "PeopleCell.h"

@interface PeopleCell ()

@property (weak, nonatomic) IBOutlet UILabel *userName;
@property (weak, nonatomic) IBOutlet UILabel *upLab;
@property (weak, nonatomic) IBOutlet UILabel *downLab;
@property (weak, nonatomic) IBOutlet UIImageView *upImg;
@property (weak, nonatomic) IBOutlet UIImageView *downImg;

@property (nonatomic ,strong) NSIndexPath *currentIndex;

@end

@implementation PeopleCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

+ (instancetype)createPeopleCellWithTableView:(UITableView *)tableView IndexPath:(nonnull NSIndexPath *)indexPath {
    PeopleCell *cell = [tableView dequeueReusableCellWithIdentifier:NSStringFromClass([self class])];
    if (!cell) {
        cell = [[PeopleCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:NSStringFromClass([self class])];
    }
    cell.currentIndex = indexPath;
    return cell;
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setModel:(UserModel *)model {
    if (self.currentIndex.row == 0) {
        self.upImg.hidden = NO;
        self.upLab.hidden = NO;
        self.upLab.text = [NSString stringWithFormat:@"%ldKb/s",(long)model.txAudioKBitrate];
        self.userName.text = [model.uid stringByAppendingString:@"(自己)"];
    } else {
        self.upImg.hidden = YES;
        self.upLab.hidden = YES;
        self.userName.text = model.uid;
    }
    self.downLab.text = [NSString stringWithFormat:@"%ldKb/s",(long)model.rxAudioKBitrate];
    
}

@end
