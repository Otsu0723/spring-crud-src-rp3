package jp.co.sss.crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.crud.entity.Employee;
import jp.co.sss.crud.form.LoginForm;
import jp.co.sss.crud.mapper.EmployeeMapper;
import jp.co.sss.crud.util.Constant;
import jp.co.sss.crud.util.LoginErrorType;

/**
 * ログイン処理
 */
@Service
public class LoginService {

	@Autowired
	private EmployeeMapper mapper;

	/**
	 * ログイン処理
	 * 
	 * mapper#findByEmpIdAndEmpPassメソッドを呼び出し、DBから該当社員を取得する。
	 * 取得した社員オブジェクトがnullの場合はログイン失敗、そうでない場合はログイン成功としてLoginResultのメソッドを呼び出す。
	 * 
	 * @return LoginResult ログイン失敗時はLoginResult.failLogin,ログイン成功時はLoginResult.succeedLoginを呼び出す。
	 */
	public LoginResult execute(LoginForm loginForm) {
		int empId = loginForm.getEmpId();
		String empPass = loginForm.getEmpPass();

		Employee loginUser = mapper.findByEmpIdAndEmpPass(empId, empPass);

		if (loginUser == null) {
			// ログイン失敗
			return LoginResult.failLogin(Constant.LOGIN_ERR_MSG, LoginErrorType.USER_NOT_FOUND);
		} else {
			// ログイン成功
			return LoginResult.succeedLogin(loginUser);
		}
	}

}
