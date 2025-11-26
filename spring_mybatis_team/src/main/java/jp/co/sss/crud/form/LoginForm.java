package jp.co.sss.crud.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginForm {

	//TODO フィールドに必要なアノテーションを付与しバリデーションを行うこと
	/** 社員ID */
	@Max(value = 99999)
	@NotNull
	private Integer empId;

	/** パスワード */
	@Size(max = 16)
	@NotBlank
	private String empPass;

	/**
	 * 社員IDの取得
	 *
	 * @return 社員ID
	 */
	public Integer getEmpId() {
		return empId;
	}

	/**
	 * 社員IDのセット
	 *
	 * @param empId 社員ID
	 */
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	/**
	 * パスワードの取得
	 *
	 * @return パスワード
	 */
	public String getEmpPass() {
		return empPass;
	}

	/**
	 * パスワードのセット
	 *
	 * @param empPass パスワード
	 */
	public void setEmpPass(String empPass) {
		this.empPass = empPass;
	}
}
