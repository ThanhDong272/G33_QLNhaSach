Use QL_NhaSach

-- Trigger

Go
Create Trigger tg_KhacHang
On KhachHang 
For insert
AS
Begin
   If exists(Select *
            From inserted I, KhachHang k
            Where I.DiaChi=k.DiaChi Or I.SoDienThoai=I.SoDienThoai)
            Begin 
             Rollback Tran
             Raiserror(N'Đã Tồn Tại',1,2)
             return
             End
END




Go
Create Trigger tg_HoaDon
on HoaDon
for Insert, Update
AS
Begin
      declare @totalct int, @tottalTon int
      Select @totalct =SUM(ct.SoLuong)
      From inserted I, HoaDon ct, BaoCaoTon T
      Where I.MaHD=ct.MaHD and ct.MaSach=T.MaSach
      group by T.SoLuongTon, I.SoLuong
      IF(@totalct> @tottalTon)
      Begin 
      Rollback Tran
      Raiserror(N'Hết Hàng',16,1)
      return
      End
 ENd
 
 ----Update
 Go
 Create Trigger tg_UpdateBaoCaoCongNo 
 On BaoCaoCongNo
 after update
 as
 Begin
    Set NOCOUNT ON;
    insert into BaoCaoCongNo
    Select *
    From deleted;
END
    
    
Go
 Create Trigger tg_UpdateBaoCaoTon
 On BaoCaoTon
 after update
 as
 Begin
    Set NOCOUNT ON;
    insert into BaoCaoTon
    Select *
    From deleted;
END


Go
 Create Trigger tg_UpdateHoaDon
 On HoaDon
 after update
 as
 Begin
    Set NOCOUNT ON;
    insert into HoaDon
    Select *
    From deleted;
END



Go
 Create Trigger tg_UpdateKhachHang
 On KhachHang
 after update
 as
 Begin
    Set NOCOUNT ON;
    insert into KhachHang
    Select *
    From deleted;
END


Go
 Create Trigger tg_UpdatePhieuNhapSach 
 On PhieuNhapSach
 after update
 as
 Begin
    Set NOCOUNT ON;
    insert into PhieuNhapSach
    Select *
    From deleted;
END


Go
 Create Trigger tg_UpdatePhieThuTien
 On PhieuThuTien
 after update
 as
 Begin
    Set NOCOUNT ON;
    insert into PhieuThuTien
    Select *
    From deleted;
END


Go
 Create Trigger tg_UpdateSach
 On Sach
 after update
 as
 Begin
    Set NOCOUNT ON;
    insert into Sach
    Select *
    From deleted;
END
  
-- Store Procedure
  
Go
Create PROC Sp_BaoCaoCongNo(@MaBCCN int,
							@IDKH int,
							@TenKH nvarchar(100),
							@NoDau money,
							@NoCuoi money,
							@PhatSinh money,
							@Thang nchar(10))

AS 
Insert into BaoCaoCongNo(IDKH,TenKH,NoDau,NoCuoi,PhatSinh,Thang)
values (@IDKH,@TenKH,@NoDau,@NoCuoi,@PhatSinh,@Thang)


Go
Create PROC Sp_BaoCaoTon(@MaBCT int,
							@MaSach int,
							@TenSach nvarchar(100),
							@SoLuongTon int,
							@TonDau int,
							@TonCuoi int,
							@Thang nchar(10))

AS 
Insert into BaoCaoTon(MaBCT,MaSach,TenSach,SoLuongTon,TonDau,TonCuoi,Thang)
values(@MaBCT,@MaSach,@TenSach,@SoLuongTon,@TonDau,@TonCuoi,@Thang)


Go
Create PROC Sp_HoaDon(@MaHD int,
							@MaSach int,
							@TenSach nvarchar(100),
							@SoLuong int,
							@DonGia money,
							@IDKH int,
							@TenKH nvarchar(100),
							@NgayLapHD date)

AS 
Insert into HoaDon(MaHD,MaSach,TenSach,SoLuong,DonGia,IDKH,TenKH,NgayLapHD)
values(@MaHD,@MaSach,@TenSach,@SoLuong,@DonGia,@IDKH,@TenKH,@NgayLapHD)

Go
Create PROC Sp_KhachHang(@IDKH int,
							@TenKH nvarchar(100),
							@GioiTinh nchar(10),
							@Email nvarchar(50),
							@SoDienThoai int,
							@DiaChi nvarchar(50))

AS 
Insert into KhachHang(IDKH,TenKH,GioiTinh,Email,SoDienThoai,DiaChi)
values(@IDKH,@TenKH,@GioiTinh,@Email,@SoDienThoai,@DiaChi)
 
 Go
Create PROC Sp_PhieuNhapSach(@MaPN int,
							@MaSach int,
							@TenSach nvarchar(100),
							@TheLoai nvarchar(50),
							@TacGia nvarchar(50),
							@SoLuong int,
							@DonGia money,
							@NgayNhap date)

AS 
Insert into PhieuNhapSach(MaPN,MaSach,TenSach,TheLoai,TacGia,SoLuong,DonGia,NgayNhap)
values(@MaPN,@MaSach,@TenSach,@TheLoai,@TacGia,@SoLuong,@DonGia,@NgayNhap)
 Go
Create PROC Sp_PhieuThuTien(@MaPT int,
							@IDKH int,
							@TenKH nvarchar(100),
							@NgayThuTien date,
							@Email nvarchar(50),
							@SoDienThoai int,
							@DiaChi nvarchar(50),
							@SoTienThu money)
							

AS 
Insert into PhieuThuTien(MaPT,IDKH,TenKH,NgayThuTien,Email,SoDienThoai,DiaChi,SoTienThu)
values(@MaPT,@IDKH,@TenKH,@NgayThuTien,@Email,@SoDienThoai,@DiaChi,@SoTienThu)
Go
Create PROC Sp_Sach(@MaSach int,
							@TenSach nvarchar(100),
							@TheLoai nvarchar(50),
							@TacGia nvarchar(100),
							@SoLuong int,
							@DonGia money,
							@NgayXuatBan date)

AS 
Insert into Sach(MaSach,TenSach,TheLoai,TacGia,SoLuong,DonGia,NgayXuatBan)
values(@MaSach,@TenSach,@TheLoai,@TacGia,@SoLuong,@DonGia,@NgayXuatBan)