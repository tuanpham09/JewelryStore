import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatDividerModule } from '@angular/material/divider';

export interface ReturnOrderDialogData {
  orderNumber: string;
  orderId: number;
}

@Component({
  selector: 'app-return-order-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatOptionModule,
    MatDividerModule
  ],
  template: `
    <div class="return-dialog">
      <div class="dialog-header">
        <div class="header-icon">
          <mat-icon>undo</mat-icon>
        </div>
        <h2 class="dialog-title">Yêu cầu trả hàng</h2>
        <p class="dialog-subtitle">Đơn hàng #{{ data.orderNumber }}</p>
      </div>

      <mat-divider></mat-divider>

      <div class="dialog-content">
        <div class="reason-section">
          <h3>Lý do trả hàng</h3>
          <p class="reason-description">Vui lòng chọn lý do trả hàng để chúng tôi có thể xử lý yêu cầu của bạn.</p>
          
          <div class="custom-select-wrapper">
            <mat-icon class="custom-select-icon">help_outline</mat-icon>
            <select class="custom-select" [(ngModel)]="selectedReason" (change)="onReasonChange()">
              <option value="">Chọn lý do trả hàng</option>
              <option value="defective">Sản phẩm bị lỗi</option>
              <option value="wrong_item">Nhận sai sản phẩm</option>
              <option value="size_wrong">Sai kích thước</option>
              <option value="color_wrong">Sai màu sắc</option>
              <option value="not_as_described">Không giống mô tả</option>
              <option value="damaged_shipping">Bị hỏng khi vận chuyển</option>
              <option value="change_mind">Thay đổi ý định</option>
              <option value="other">Lý do khác</option>
            </select>
          </div>

          <div class="custom-input-wrapper" *ngIf="selectedReason === 'other'">
            <mat-icon class="custom-input-icon">edit</mat-icon>
            <textarea 
              class="custom-input custom-textarea" 
              [(ngModel)]="customReason" 
              placeholder="Vui lòng mô tả lý do trả hàng..."
              maxlength="500"
            ></textarea>
            <div class="char-counter">{{ customReason.length }}/500</div>
          </div>

          <div class="custom-input-wrapper" *ngIf="selectedReason !== 'other' && selectedReason">
            <mat-icon class="custom-input-icon">note</mat-icon>
            <textarea 
              class="custom-input custom-textarea" 
              [(ngModel)]="additionalNotes" 
              placeholder="Mô tả thêm về tình trạng sản phẩm..."
              maxlength="300"
            ></textarea>
            <div class="char-counter">{{ additionalNotes.length }}/300</div>
          </div>
        </div>

        <div class="return-info-section">
          <div class="info-box">
            <mat-icon class="info-icon">info</mat-icon>
            <div class="info-content">
              <h4>Thông tin trả hàng</h4>
              <ul>
                <li>Chúng tôi sẽ liên hệ trong vòng 24h để xác nhận</li>
                <li>Bạn có thể trả hàng trong vòng 7 ngày kể từ khi nhận</li>
                <li>Tiền hoàn sẽ được chuyển trong 3-5 ngày làm việc</li>
                <li>Phí vận chuyển sẽ được hoàn nếu sản phẩm có lỗi</li>
              </ul>
            </div>
          </div>
        </div>
      </div>

      <mat-divider></mat-divider>

      <div class="dialog-actions">
        <button class="custom-btn custom-btn-secondary" (click)="onCancel()">
          <mat-icon>close</mat-icon>
          Hủy
        </button>
        <button 
          class="custom-btn" 
          [disabled]="!isValidReason()"
          (click)="onConfirm()"
        >
          <mat-icon>send</mat-icon>
          Gửi yêu cầu
        </button>
      </div>
    </div>
  `,
  styles: [`
    .return-dialog {
      max-width: 600px;
      width: 100%;
      max-height: 80vh;
      overflow-y: auto;
    }

    .dialog-header {
      text-align: center;
      padding: 1.5rem 2rem 1rem 2rem;
      background: linear-gradient(135deg, #e3f2fd 0%, #f3e5f5 100%);
      border-radius: 15px 15px 0 0;
    }

    .header-icon {
      width: 50px;
      height: 50px;
      background: linear-gradient(135deg, #2196f3, #9c27b0);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 1rem auto;
      box-shadow: 0 4px 15px rgba(33, 150, 243, 0.3);
    }

    .header-icon mat-icon {
      color: white;
      font-size: 24px;
      width: 24px;
      height: 24px;
    }

    .dialog-title {
      margin: 0 0 0.5rem 0;
      font-size: 1.3rem;
      font-weight: 500;
      color: #333;
    }

    .dialog-subtitle {
      margin: 0;
      color: #666;
      font-size: 0.9rem;
    }

    .dialog-content {
      padding: 1.5rem 2rem;
    }

    .reason-section h3 {
      margin: 0 0 0.5rem 0;
      font-size: 1rem;
      font-weight: 500;
      color: #333;
    }

    .reason-description {
      margin: 0 0 1rem 0;
      color: #666;
      font-size: 0.85rem;
      line-height: 1.4;
    }

    /* Custom Input/Select Styles */
    .custom-select-wrapper, .custom-input-wrapper {
      position: relative;
      display: flex;
      align-items: center;
      background: white;
      border-radius: 25px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
      border: 2px solid transparent;
      transition: all 0.3s ease;
      overflow: hidden;
      margin-bottom: 1rem;
    }

    .custom-select-wrapper:focus-within, .custom-input-wrapper:focus-within {
      border-color: #2196f3;
      box-shadow: 0 8px 30px rgba(33, 150, 243, 0.15);
      transform: translateY(-2px);
    }

    .custom-select-icon, .custom-input-icon {
      position: absolute;
      left: 20px;
      color: #999;
      font-size: 18px;
      z-index: 2;
    }

    .custom-select, .custom-input {
      flex: 1;
      padding: 14px 20px 14px 50px;
      border: none;
      outline: none;
      font-size: 14px;
      background: transparent;
      color: #333;
      font-weight: 400;
      width: 100%;
    }

    .custom-textarea {
      resize: vertical;
      min-height: 80px;
      padding-top: 16px;
      padding-bottom: 16px;
    }

    .custom-select {
      appearance: none;
      cursor: pointer;
    }

    .custom-select option {
      color: #333;
      background: white;
    }

    .char-counter {
      position: absolute;
      bottom: 8px;
      right: 12px;
      font-size: 11px;
      color: #999;
      background: white;
      padding: 2px 6px;
      border-radius: 10px;
    }

    .return-info-section {
      margin-top: 1.5rem;
    }

    .info-box {
      background: #e8f5e8;
      border: 1px solid #4caf50;
      border-radius: 15px;
      padding: 1rem;
      display: flex;
      gap: 1rem;
      align-items: flex-start;
    }

    .info-icon {
      color: #4caf50;
      font-size: 20px;
      width: 20px;
      height: 20px;
      margin-top: 2px;
    }

    .info-content h4 {
      margin: 0 0 0.5rem 0;
      font-size: 0.85rem;
      font-weight: 500;
      color: #2e7d32;
    }

    .info-content ul {
      margin: 0;
      padding-left: 1rem;
      color: #666;
      font-size: 0.8rem;
      line-height: 1.4;
    }

    .info-content li {
      margin-bottom: 0.25rem;
    }

    .dialog-actions {
      display: flex;
      gap: 1rem;
      padding: 1rem 2rem 1.5rem 2rem;
      justify-content: center;
    }

    .custom-btn {
      background: linear-gradient(135deg, #2196f3, #9c27b0);
      color: white;
      border: none;
      border-radius: 25px;
      padding: 10px 24px;
      font-size: 14px;
      font-weight: 500;
      cursor: pointer;
      transition: all 0.3s ease;
      box-shadow: 0 4px 15px rgba(33, 150, 243, 0.3);
      display: flex;
      align-items: center;
      gap: 8px;
      min-width: 120px;
      justify-content: center;
    }

    .custom-btn:hover:not(:disabled) {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(33, 150, 243, 0.4);
    }

    .custom-btn:disabled {
      background: #ccc;
      color: #999;
      box-shadow: none;
      cursor: not-allowed;
    }

    .custom-btn-secondary {
      background: linear-gradient(135deg, #6c757d, #868e96);
      box-shadow: 0 4px 15px rgba(108, 117, 125, 0.3);
    }

    .custom-btn-secondary:hover {
      box-shadow: 0 8px 25px rgba(108, 117, 125, 0.4);
    }
  `]
})
export class ReturnOrderDialogComponent {
  selectedReason: string = '';
  customReason: string = '';
  additionalNotes: string = '';

  constructor(
    public dialogRef: MatDialogRef<ReturnOrderDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ReturnOrderDialogData
  ) {}

  onReasonChange() {
    if (this.selectedReason !== 'other') {
      this.customReason = '';
    }
  }

  isValidReason(): boolean {
    if (this.selectedReason === 'other') {
      return this.customReason.trim().length >= 10;
    }
    return this.selectedReason.length > 0;
  }

  onCancel() {
    this.dialogRef.close();
  }

  onConfirm() {
    let finalReason = '';
    
    if (this.selectedReason === 'other') {
      finalReason = this.customReason.trim();
    } else {
      const reasonMap: { [key: string]: string } = {
        'defective': 'Sản phẩm bị lỗi',
        'wrong_item': 'Nhận sai sản phẩm',
        'size_wrong': 'Sai kích thước',
        'color_wrong': 'Sai màu sắc',
        'not_as_described': 'Không giống mô tả',
        'damaged_shipping': 'Bị hỏng khi vận chuyển',
        'change_mind': 'Thay đổi ý định'
      };
      finalReason = reasonMap[this.selectedReason] || this.selectedReason;
    }

    if (this.additionalNotes.trim()) {
      finalReason += ` (${this.additionalNotes.trim()})`;
    }

    this.dialogRef.close({
      confirmed: true,
      reason: finalReason
    });
  }
}
