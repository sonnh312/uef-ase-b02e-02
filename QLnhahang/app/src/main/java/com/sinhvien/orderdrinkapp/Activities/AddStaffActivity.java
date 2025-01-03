package com.sinhvien.orderdrinkapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.sinhvien.orderdrinkapp.DAO.NhanVienDAO;
import com.sinhvien.orderdrinkapp.DTO.NhanVienDTO;
import com.sinhvien.orderdrinkapp.R;

import java.util.Calendar;
import java.util.regex.Pattern;

public class AddStaffActivity extends AppCompatActivity implements View.OnClickListener {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=\\S+$)" +            // no white spaces
                    ".{6,}" +                // at least 6 characters
                    "$");

    ImageView IMG_addstaff_back;
    TextView TXT_addstaff_title;
    TextInputLayout TXTL_addstaff_HoVaTen, TXTL_addstaff_TenDN, TXTL_addstaff_Email, TXTL_addstaff_SDT, TXTL_addstaff_MatKhau;
    RadioGroup RG_addstaff_GioiTinh, rg_addstaff_Quyen;
    RadioButton RD_addstaff_Nam, RD_addstaff_Nu, RD_addstaff_Khac, rd_addstaff_QuanLy, rd_addstaff_NhanVien;
    DatePicker DT_addstaff_NgaySinh;
    Button BTN_addstaff_ThemNV;
    NhanVienDAO nhanVienDAO;
    String hoTen, tenDN, eMail, sDT, matKhau, gioiTinh, ngaySinh, viTri, moTa;
    int manv = 0, quyen = 0;
    long ktra = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addstaff_layout);

        //region Lấy đối tượng trong view
        TXT_addstaff_title = findViewById(R.id.txt_addstaff_title);
        IMG_addstaff_back = findViewById(R.id.img_addstaff_back);
        TXTL_addstaff_HoVaTen = findViewById(R.id.txtl_addstaff_HoVaTen);
        TXTL_addstaff_TenDN = findViewById(R.id.txtl_addstaff_TenDN);
        TXTL_addstaff_Email = findViewById(R.id.txtl_addstaff_Email);
        TXTL_addstaff_SDT = findViewById(R.id.txtl_addstaff_SDT);
        TXTL_addstaff_MatKhau = findViewById(R.id.txtl_addstaff_MatKhau);
        RG_addstaff_GioiTinh = findViewById(R.id.rg_addstaff_GioiTinh);
        rg_addstaff_Quyen = findViewById(R.id.rg_addstaff_Quyen);
        RD_addstaff_Nam = findViewById(R.id.rd_addstaff_Nam);
        RD_addstaff_Nu = findViewById(R.id.rd_addstaff_Nu);
        RD_addstaff_Khac = findViewById(R.id.rd_addstaff_Khac);
        rd_addstaff_QuanLy = findViewById(R.id.rd_addstaff_QuanLy);
        rd_addstaff_NhanVien = findViewById(R.id.rd_addstaff_NhanVien);
        DT_addstaff_NgaySinh = findViewById(R.id.dt_addstaff_NgaySinh);
        BTN_addstaff_ThemNV = findViewById(R.id.btn_addstaff_ThemNV);
        //endregion

        nhanVienDAO = new NhanVienDAO(this);

        //region Hiển thị trang sửa nếu được chọn từ context menu sửa
        manv = getIntent().getIntExtra("manv", 0);
        if (manv != 0) {
            TXT_addstaff_title.setText("Sửa nhân viên");
            NhanVienDTO nhanVienDTO = nhanVienDAO.LayNVTheoMa(manv);

            // Hiển thị thông tin từ csdl
            TXTL_addstaff_HoVaTen.getEditText().setText(nhanVienDTO.getHOTENNV());
            TXTL_addstaff_TenDN.getEditText().setText(nhanVienDTO.getTENDN());
            TXTL_addstaff_Email.getEditText().setText(nhanVienDTO.getEMAIL());
            TXTL_addstaff_SDT.getEditText().setText(nhanVienDTO.getSDT());
            TXTL_addstaff_MatKhau.getEditText().setText(nhanVienDTO.getMATKHAU());



            // Hiển thị giới tính
            String gioitinh = nhanVienDTO.getGIOITINH();
            if (gioitinh.equals("Nam")) {
                RD_addstaff_Nam.setChecked(true);
            } else if (gioitinh.equals("Nữ")) {
                RD_addstaff_Nu.setChecked(true);
            } else {
                RD_addstaff_Khac.setChecked(true);
            }

            // Hiển thị quyền
            if (nhanVienDTO.getMAQUYEN() == 1) {
                rd_addstaff_QuanLy.setChecked(true);
            } else {
                rd_addstaff_NhanVien.setChecked(true);
            }

            // Hiển thị ngày sinh
            String date = nhanVienDTO.getNGAYSINH();
            String[] items = date.split("/");
            int day = Integer.parseInt(items[0]);
            int month = Integer.parseInt(items[1]) - 1;
            int year = Integer.parseInt(items[2]);
            DT_addstaff_NgaySinh.updateDate(year, month, day);
            BTN_addstaff_ThemNV.setText("Sửa nhân viên");
        }
        //endregion

        BTN_addstaff_ThemNV.setOnClickListener(this);
        IMG_addstaff_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String chucnang;
        switch (id) {
            case R.id.btn_addstaff_ThemNV:
                if (!validateAllFields()) return;

                // Lấy dữ liệu từ view
                hoTen = TXTL_addstaff_HoVaTen.getEditText().getText().toString();
                tenDN = TXTL_addstaff_TenDN.getEditText().getText().toString();
                eMail = TXTL_addstaff_Email.getEditText().getText().toString();
                sDT = TXTL_addstaff_SDT.getEditText().getText().toString();
                matKhau = TXTL_addstaff_MatKhau.getEditText().getText().toString();


                switch (RG_addstaff_GioiTinh.getCheckedRadioButtonId()) {
                    case R.id.rd_addstaff_Nam:
                        gioiTinh = "Nam";
                        break;
                    case R.id.rd_addstaff_Nu:
                        gioiTinh = "Nữ";
                        break;
                    case R.id.rd_addstaff_Khac:
                        gioiTinh = "Khác";
                        break;
                }
                switch (rg_addstaff_Quyen.getCheckedRadioButtonId()) {
                    case R.id.rd_addstaff_QuanLy:
                        quyen = 1;
                        break;
                    case R.id.rd_addstaff_NhanVien:
                        quyen = 2;
                        break;
                }

                ngaySinh = DT_addstaff_NgaySinh.getDayOfMonth() + "/" + (DT_addstaff_NgaySinh.getMonth() + 1)
                        + "/" + DT_addstaff_NgaySinh.getYear();

                // Truyền dữ liệu vào obj nhanvienDTO
                NhanVienDTO nhanVienDTO = new NhanVienDTO();
                nhanVienDTO.setHOTENNV(hoTen);
                nhanVienDTO.setTENDN(tenDN);
                nhanVienDTO.setEMAIL(eMail);
                nhanVienDTO.setSDT(sDT);
                nhanVienDTO.setMATKHAU(matKhau);
                nhanVienDTO.setGIOITINH(gioiTinh);
                nhanVienDTO.setNGAYSINH(ngaySinh);
                nhanVienDTO.setMAQUYEN(quyen);
                nhanVienDTO.setVITRI(viTri);
                nhanVienDTO.setMOTA(moTa);

                if (manv != 0) {
                    ktra = nhanVienDAO.SuaNhanVien(nhanVienDTO, manv);
                    chucnang = "Sửa";
                } else {
                    ktra = nhanVienDAO.ThemNhanVien(nhanVienDTO);
                    chucnang = "Thêm";
                }
                if (ktra != 0) {
                    Toast.makeText(this, chucnang + " thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, chucnang + " thất bại!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.img_addstaff_back:
                finish();
                break;
        }
    }

    private boolean validateAllFields() {
        if (!validateHoVaTen() | !validateTenDN() | !validateEmail() | !validateSDT() | !validateMatKhau() ) {
            return false;
        }
        return true;
    }

    private boolean validateHoVaTen() {
        String hoVaTen = TXTL_addstaff_HoVaTen.getEditText().getText().toString();
        if (hoVaTen.isEmpty()) {
            TXTL_addstaff_HoVaTen.setError("Họ và tên không được để trống!");
            return false;
        }
        TXTL_addstaff_HoVaTen.setError(null);
        return true;
    }

    private boolean validateTenDN() {
        String tenDN = TXTL_addstaff_TenDN.getEditText().getText().toString();
        if (tenDN.isEmpty()) {
            TXTL_addstaff_TenDN.setError("Tên đăng nhập không được để trống!");
            return false;
        }
        TXTL_addstaff_TenDN.setError(null);
        return true;
    }

    private boolean validateEmail() {
        String email = TXTL_addstaff_Email.getEditText().getText().toString();
        if (email.isEmpty()) {
            TXTL_addstaff_Email.setError("Email không được để trống!");
            return false;
        }
        TXTL_addstaff_Email.setError(null);
        return true;
    }

    private boolean validateSDT() {
        String sdt = TXTL_addstaff_SDT.getEditText().getText().toString();
        if (sdt.isEmpty()) {
            TXTL_addstaff_SDT.setError("Số điện thoại không được để trống!");
            return false;
        }
        TXTL_addstaff_SDT.setError(null);
        return true;
    }

    private boolean validateMatKhau() {
        String matKhau = TXTL_addstaff_MatKhau.getEditText().getText().toString();
        if (matKhau.isEmpty()) {
            TXTL_addstaff_MatKhau.setError("Mật khẩu không được để trống!");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(matKhau).matches()) {
            TXTL_addstaff_MatKhau.setError("Mật khẩu phải ít nhất 6 ký tự, không chứa khoảng trắng!");
            return false;
        }
        TXTL_addstaff_MatKhau.setError(null);
        return true;
    }

}
