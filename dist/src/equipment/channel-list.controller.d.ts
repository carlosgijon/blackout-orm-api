import { ChannelListService } from './channel-list.service';
export declare class ChannelListController {
    private readonly channelList;
    constructor(channelList: ChannelListService);
    generate(user: any): Promise<any[]>;
}
