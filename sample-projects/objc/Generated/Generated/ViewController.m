//
//  ViewController.m
//  Generated
//
//  Created by Eric Internicola on 2/25/15.
//  Copyright (c) 2015 intere. All rights reserved.
//

#import "ViewController.h"
#import "ContestView.h"
#import "MockDataService.h"

@interface ViewController () {
    ContestView *contestView;
}
@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    contestView = [[ContestView alloc]initWithFrame:self.view.frame];
    [self.scrollView addSubview:contestView];
    [self.scrollView setContentSize:contestView.frame.size];
    contestView.contest = [[MockDataService getSharedInstance].contests objectAtIndex:0];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
