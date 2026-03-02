import { Controller, Get, Post, Put, Patch, Delete, Body, Param, UseGuards } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { GigsService } from './gigs.service';
import { JwtAuthGuard } from '../auth/guards/jwt.guard';
import { BandGuard } from '../common/guards/band.guard';
import { CurrentUser } from '../common/decorators/current-user.decorator';

@ApiTags('gigs')
@ApiBearerAuth('JWT')
@Controller('gigs')
@UseGuards(JwtAuthGuard, BandGuard)
export class GigsController {
  constructor(private readonly gigs: GigsService) {}

  // -- Gigs --

  @Get()
  findAll(@CurrentUser() user: any) { return this.gigs.findAll(user.bandId); }

  @Post()
  create(@CurrentUser() user: any, @Body() dto: any) { return this.gigs.create(user.bandId, dto); }

  @Put(':id')
  update(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.gigs.update(user.bandId, id, dto);
  }

  @Patch(':id/status')
  updateStatus(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: { status: string }) {
    return this.gigs.updateStatus(user.bandId, id, dto.status);
  }

  @Patch(':id/follow-up')
  updateFollowUp(@CurrentUser() user: any, @Param('id') id: string, @Body() dto: any) {
    return this.gigs.updateFollowUp(user.bandId, id, dto);
  }

  @Delete(':id')
  remove(@CurrentUser() user: any, @Param('id') id: string) { return this.gigs.remove(user.bandId, id); }

  // -- Contacts --

  @Get(':gigId/contacts')
  getContacts(@CurrentUser() user: any, @Param('gigId') gigId: string) {
    return this.gigs.getContacts(user.bandId, gigId);
  }

  @Post(':gigId/contacts')
  createContact(@CurrentUser() user: any, @Param('gigId') gigId: string, @Body() dto: any) {
    return this.gigs.createContact(user.bandId, gigId, dto);
  }

  @Delete(':gigId/contacts/:contactId')
  removeContact(@CurrentUser() user: any, @Param('gigId') gigId: string, @Param('contactId') contactId: string) {
    return this.gigs.removeContact(user.bandId, gigId, contactId);
  }

  // -- Checklists --

  @Get(':gigId/checklists')
  getChecklists(@CurrentUser() user: any, @Param('gigId') gigId: string) {
    return this.gigs.getChecklists(user.bandId, gigId);
  }

  @Post(':gigId/checklists')
  createChecklist(@CurrentUser() user: any, @Param('gigId') gigId: string, @Body() dto: { name: string }) {
    return this.gigs.createChecklist(user.bandId, gigId, dto);
  }

  @Delete(':gigId/checklists/:checklistId')
  removeChecklist(
    @CurrentUser() user: any,
    @Param('gigId') gigId: string,
    @Param('checklistId') checklistId: string,
  ) {
    return this.gigs.removeChecklist(user.bandId, gigId, checklistId);
  }

  // -- Checklist Items --

  @Get('checklists/:checklistId/items')
  getItems(@CurrentUser() user: any, @Param('checklistId') checklistId: string) {
    return this.gigs.getItems(user.bandId, checklistId);
  }

  @Post('checklists/:checklistId/items')
  createItem(@CurrentUser() user: any, @Param('checklistId') checklistId: string, @Body() dto: any) {
    return this.gigs.createItem(user.bandId, checklistId, dto);
  }

  @Put('checklists/:checklistId/items/:itemId')
  updateItem(
    @CurrentUser() user: any,
    @Param('checklistId') checklistId: string,
    @Param('itemId') itemId: string,
    @Body() dto: any,
  ) {
    return this.gigs.updateItem(user.bandId, checklistId, itemId, dto);
  }

  @Delete('checklists/:checklistId/items/:itemId')
  removeItem(
    @CurrentUser() user: any,
    @Param('checklistId') checklistId: string,
    @Param('itemId') itemId: string,
  ) {
    return this.gigs.removeItem(user.bandId, checklistId, itemId);
  }

  @Post('checklists/:checklistId/reset')
  resetItems(@CurrentUser() user: any, @Param('checklistId') checklistId: string) {
    return this.gigs.resetItems(user.bandId, checklistId);
  }

  // -- By-ID deletes --

  @Delete('contacts/:contactId')
  removeContactById(@CurrentUser() user: any, @Param('contactId') contactId: string) {
    return this.gigs.removeContactById(user.bandId, contactId);
  }

  @Delete('checklists/:checklistId')
  removeChecklistById(@CurrentUser() user: any, @Param('checklistId') checklistId: string) {
    return this.gigs.removeChecklistById(user.bandId, checklistId);
  }

  @Delete('checklist-items/:itemId')
  removeItemById(@CurrentUser() user: any, @Param('itemId') itemId: string) {
    return this.gigs.removeItemById(user.bandId, itemId);
  }
}
