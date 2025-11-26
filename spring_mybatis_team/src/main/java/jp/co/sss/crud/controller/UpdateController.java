package jp.co.sss.crud.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.crud.entity.Department;
import jp.co.sss.crud.entity.Employee;
import jp.co.sss.crud.form.EmployeeForm;
import jp.co.sss.crud.service.SearchForDepartmentByDeptIdService;
import jp.co.sss.crud.service.SearchForEmployeesByEmpIdService;
import jp.co.sss.crud.service.UpdateEmployeeService;
import jp.co.sss.crud.util.Constant;

/**
 * 社員更新コントローラー
 */
@Controller
public class UpdateController {

	/**
	 * 社員IDを基に社員情報を検索するサービス
	 */
	@Autowired
	SearchForEmployeesByEmpIdService searchForEmployeesByEmpIdService;

	/**
	 * 社員情報を更新するサービス
	 */
	@Autowired
	UpdateEmployeeService updateEmployeeService;

	/**
	 * 部署IDを基に部署情報を検索するサービス
	 */
	@Autowired
	SearchForDepartmentByDeptIdService searchForDepartmentByDeptIdService;

	/**
	 * 社員情報の変更内容入力画面を出力
	 *
	 * @param empId
	 *            社員ID
	 * @param model
	 *            モデル
	 * @return 遷移先のビュー
	 */
	@RequestMapping(path = "/update/input", method = RequestMethod.GET)
	public String inputUpdate(Integer empId, @ModelAttribute EmployeeForm employeeForm, HttpSession session) {
		if (session.getAttribute("user") == null) {
			session.setAttribute("user", new Employee());
		}
		// 社員IDに紐づく社員情報を検索し、Employee型の変数に代入する
		Employee employee = searchForEmployeesByEmpIdService.execute(empId);
		// 検索した社員情報をformに積め直す(BeanCopyクラスを用いてもよい)	
		employeeForm.setEmpId(employee.getEmpId());
		employeeForm.setEmpPass(employee.getEmpPass());
		employeeForm.setEmpName(employee.getEmpName());
		employeeForm.setGender(employee.getGender());
		employeeForm.setAddress(employee.getAddress());
		employeeForm.setBirthday(employee.getBirthday());
		employeeForm.setAuthority(employee.getAuthority());
		employeeForm.setDeptId(employee.getDeptId());
		// 更新確認画面のビュー名を返す
		return "update/update_input";
	}

	/**
	 * 社員情報の変更確認画面を出力
	 *
	 * @param employeeForm
	 *            変更対象の社員情報
	 * @param model
	 *            モデル
	 * @return 遷移先のビュー
	 */
	@RequestMapping(path = "/update/check", method = RequestMethod.POST)
	public String checkUpdate(@Valid @ModelAttribute EmployeeForm employeeForm, BindingResult result, Model model) {
		// 入力チェックでエラーが発生した場合
		if (result.hasErrors()) {
			// エラーがある場合は入力画面に戻る
			return "update/update_input";
		} else {
			// 部署IDから部署情報を検索する
			Department department = searchForDepartmentByDeptIdService.execute(employeeForm.getDeptId());
			// 部署名をモデルに追加する
			model.addAttribute("deptName", department.getDeptName());
			// 更新確認画面のビュー名を返す
			return "update/update_check";
		}
	}

	/**
	 * 変更内容入力画面に戻る
	 *
	 * @param employeeForm 変更対象の社員情報
	 * @return 遷移先のビュー
	 */
	@RequestMapping(path = "/update/back", method = RequestMethod.POST)
	public String backInputUpdate(@ModelAttribute EmployeeForm employeeForm) {
		//  更新入力画面のビュー名を返す
		return "update/update_input";
	}

	/**
	 * 社員情報の変更実行
	 *
	 * @param employeeForm
	 *            変更対象の社員情報
	 * @return 完了画面URLへリダイレクト
	 */
	@RequestMapping(path = "/update/complete", method = RequestMethod.POST)
	public String completeUpdate(EmployeeForm employeeForm, HttpSession session) {

		// フォームの内容をEmployeeエンティティにコピー
		Employee employee = new Employee();
		employee.setEmpId(employeeForm.getEmpId());
		employee.setEmpPass(employeeForm.getEmpPass());
		employee.setEmpName(employeeForm.getEmpName());
		employee.setGender(employeeForm.getGender());
		employee.setAddress(employeeForm.getAddress());
		employee.setBirthday(employeeForm.getBirthday());
		employee.setAuthority(employeeForm.getAuthority());
		employee.setDeptId(employeeForm.getDeptId());
		// 権限がnullの場合、デフォルトの権限を設定
		if (employee.getAuthority() == null) {
			employee.setAuthority(Constant.DEFAULT_AUTHORITY);
		}

		// 社員情報を更新する
		updateEmployeeService.execute(employee);
		// セッションからユーザー情報を取得
		Employee user = (Employee) session.getAttribute("user");
		// ログイン中のユーザーが自分の情報を更新した場合、セッション情報も更新
		if (user != null && user.getEmpId().equals(employee.getEmpId())) {
			// セッションに保存されているユーザーの社員名を更新
			user.setEmpPass(employee.getEmpPass());
			user.setEmpName(employee.getEmpName());
			user.setGender(employee.getGender());
			user.setAddress(employee.getAddress());
			user.setBirthday(employee.getBirthday());
			user.setAuthority(employee.getAuthority());
			user.setDeptId(employee.getDeptId());

			session.setAttribute("user", user);
		}

		// 更新完了画面へリダイレクト
		return "redirect:/update/complete";
	}

	/**
	 * 社員情報の変更完了画面
	 *
	 * @return 遷移先のビュー
	 */
	@RequestMapping(path = "/update/complete", method = RequestMethod.GET)
	public String completeUpdate() {
		//  更新完了画面のビュー名を返す
		return "update/update_complete";

	}

}
