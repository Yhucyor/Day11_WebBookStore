package thuc.ute.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime; // Nhớ import dòng này ở đầu file
@Entity
@Table(name = "users") // Ánh xạ đúng tên bảng của bạnimport java.io.Serializable;

public class User_24110349 implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Khóa chính tăng tự động
    @Column(name = "id")
    private int id;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "fullname", columnDefinition = "nvarchar(50)")
    private String fullname;

    // Sử dụng Integer thay vì int vì trong ảnh cột này Allow Nulls
    @Column(name = "phone")
    private Integer phone;

    @Column(name = "passwd", length = 32)
    private String passwd;
    
    // Thời gian đăng kí 
    @Column(name = "signup_date")
    private LocalDateTime signupDate;

    //Thời gian lần cuối đăng nhập 
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // Kiểu 'bit' trong SQL Server tương ứng với Boolean trong Java
    // Sử dụng Boolean (class wrapper) vì cột này Allow Nulls
    @Column(name = "is_admin")
    private Boolean isAdmin;

    public User_24110349() {
        super();
    }

	public User_24110349(int id, String email, String fullname, Integer phone, String passwd, LocalDateTime signupDate,
			LocalDateTime lastLogin, Boolean isAdmin) {
		super();
		this.id = id;
		this.email = email;
		this.fullname = fullname;
		this.phone = phone;
		this.passwd = passwd;
		this.signupDate = signupDate;
		this.lastLogin = lastLogin;
		this.isAdmin = isAdmin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Integer getPhone() {
		return phone;
	}

	public void setPhone(Integer phone) {
		this.phone = phone;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public LocalDateTime getSignupDate() {
		return signupDate;
	}

	public void setSignupDate(LocalDateTime signupDate) {
		this.signupDate = signupDate;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
}
