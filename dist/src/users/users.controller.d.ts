import { UsersService } from './users.service';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
import { ChangePasswordDto } from './dto/change-password.dto';
export declare class UsersController {
    private readonly users;
    constructor(users: UsersService);
    findAll(user: any): Promise<any[]>;
    create(user: any, dto: CreateUserDto): Promise<any>;
    update(user: any, id: string, dto: UpdateUserDto): Promise<any>;
    changePassword(user: any, id: string, dto: ChangePasswordDto): Promise<void>;
    remove(user: any, id: string): Promise<void>;
}
