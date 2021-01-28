//
//  RoomViewController.m
//  AR-iOS-Voice-Base
//
//  Created by anyRTC on 2021/1/25.
//

#define APPID               @""


#import "RoomViewController.h"
#import "PeopleCell.h"
#import "UserModel.h"
@interface RoomViewController ()<UITableViewDelegate,UITableViewDataSource,ARtcEngineDelegate>
@property (weak, nonatomic) IBOutlet UITableView *table;
@property (weak, nonatomic) IBOutlet UIButton *voiceBtn;
@property (weak, nonatomic) IBOutlet UIButton *closeBtn;
@property (weak, nonatomic) IBOutlet UIButton *micBtn;

@property (nonatomic ,strong) ARtcEngineKit *engineKit;

@property (nonatomic ,strong) NSMutableArray *userList;

@end

@implementation RoomViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = self.channelId;
    [self initARtcKit];
    [self initUI];
    [self.engineKit joinChannelByToken:@"" channelId:self.channelId uid:self.userId joinSuccess:nil];
}

-(void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    [self.engineKit leaveChannel:nil];
    self.engineKit.delegate = nil;
    self.engineKit = nil;
}


- (NSMutableArray *)userList {
    if (!_userList) {
        _userList = [NSMutableArray new];
    }
    return _userList;
}

- (void)initUI {
    self.table.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.table setTableFooterView:[[UIView alloc] init]];
    
    
    [self.micBtn setImage:IMG(@"img_audio_open") forState:UIControlStateNormal];
    [self.micBtn setImage:IMG(@"img_audio_close") forState:UIControlStateSelected];
    
    [self.voiceBtn setImage:IMG(@"img_voice_open") forState:UIControlStateNormal];
    [self.voiceBtn setImage:IMG(@"img_voice_close") forState:UIControlStateSelected];
}

- (void)initARtcKit {
    self.engineKit = [ARtcEngineKit sharedEngineWithAppId:APPID delegate:self];
    [self.engineKit setEnableSpeakerphone:YES];
    [self.engineKit setChannelProfile:ARChannelProfileCommunication];
}


//MARK: - UITableViewDelegate/UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.userList.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    PeopleCell *cell = [PeopleCell createPeopleCellWithTableView:tableView IndexPath:indexPath];
    cell.model = self.userList[indexPath.row];
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 50;
}

//MARK: - ARtcEngineDelegate

///用户加入RTC频道回调
- (void)rtcEngine:(ARtcEngineKit *)engine didJoinChannel:(NSString *)channel withUid:(NSString *)uid elapsed:(NSInteger)elapsed {
    UserModel *model = [[UserModel alloc] init];
    model.uid = uid;
    [self.userList addObject:model];
    [self.table reloadData];
}

///用户离开RTC频道回调
- (void)rtcEngine:(ARtcEngineKit * _Nonnull)engine didLeaveChannelWithStats:(ARChannelStats * _Nonnull)stats{
    
}

///远端用户加入RTC频道回调
- (void)rtcEngine:(ARtcEngineKit *)engine didJoinedOfUid:(NSString *)uid elapsed:(NSInteger)elapsed {
    UserModel *model = [[UserModel alloc] init];
    model.uid = uid;
    [self.userList addObject:model];
    [self.table reloadData];
}

///远端用户离开RTC频道回调
- (void)rtcEngine:(ARtcEngineKit *_Nonnull)engine didOfflineOfUid:(NSString *_Nonnull)uid reason:(ARUserOfflineReason)reason {
    for (UserModel *model in self.userList) {
        if ([model.uid isEqualToString:uid]) {
            [self.userList removeObject:model];
            break;
        }
    }
    [self.table reloadData];
}

///本地用户通话信息流的统计
- (void)rtcEngine:(ARtcEngineKit * _Nonnull)engine reportRtcStats:(ARChannelStats * _Nonnull)stats {
    for (UserModel *model in self.userList) {
        if ([model.uid isEqualToString:self.userId]) {
            model.rxAudioKBitrate = stats.rxAudioKBitrate;
            model.txAudioKBitrate = stats.txAudioKBitrate;
        }
    }
    [self.table reloadData];
}

///远端用户通话信息流的统计
- (void)rtcEngine:(ARtcEngineKit * _Nonnull)engine remoteAudioStats:(ARtcRemoteAudioStats * _Nonnull)stats {
    for (UserModel *model in self.userList) {
        if ([model.uid isEqualToString:stats.uid]) {
            model.rxAudioKBitrate = stats.receivedBitrate;
        }
    }
    [self.table reloadData];
}


- (IBAction)VoiceAction:(id)sender {
    self.voiceBtn.selected = !self.voiceBtn.selected;
    [self.engineKit setEnableSpeakerphone:!self.voiceBtn.selected];
}

- (IBAction)CloseAction:(id)sender {
    [self.engineKit leaveChannel:nil];
    self.engineKit.delegate = nil;
    self.engineKit = nil;
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)MicAction:(id)sender {
    self.micBtn.selected = !self.micBtn.selected;
    [self.engineKit enableLocalAudio:!self.micBtn.selected];
}

@end
