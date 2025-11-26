package jp.co.sss.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.crud.entity.Employee;
import jp.co.sss.crud.service.DeleteEmployeeService;
import jp.co.sss.crud.service.SearchForDepartmentByDeptIdService;
import jp.co.sss.crud.service.SearchForEmployeesByEmpIdService;

/**
 * 社員削除コントローラー
 * 
 */
@Controller
public class DeleteController {

	/**
	 * 社員IDを基に社員情報を検索するサービス
	 */
	@Autowired
	SearchForEmployeesByEmpIdService searchForEmployeesByEmpIdService;

	/**
	 * 社員削除を行うサービス
	 */
	@Autowired
	DeleteEmployeeService deleteEmployeeService;

	/**
	 * 部署IDを基に部署情報を検索するサービス
	 */
	@Autowired
	SearchForDepartmentByDeptIdService searchForDepartmentByDeptIdService;

	/**
	 * 社員情報の削除内容確認画面を出力
	 *
	 * @param empId 社員ID
	 * @param model モデル
	 * @return 遷移先のビュー
	 */
	@RequestMapping(path = "/delete/check", method = RequestMethod.GET)
	public String checkDelete(Integer empId, Model model) {

		// TODO 社員IDに紐づく社員情報を検索し、Employee型の変数に代入する
		Employee employee = searchForEmployeesByEmpIdService.execute(empId);
		// TODO 取得した社員情報をモデルに追加する
		model.addAttribute("employee", employee);
		// TODO 取得した社員の部署名をモデルに追加する
		model.addAttribute("deptName", employee.getDeptName());
		// 削除確認画面のビュー名を返す
		return "delete/delete_check";
	}

	/**
	 * 社員情報の削除を行う
	 *
	 * @param empId 社員ID
	 * @param model モデル
	 * @return リダイレクト先のURL
	 */
	@RequestMapping(path = "/delete/complete", method = RequestMethod.POST)
	public String completeDelete(Integer empId) {
		// TODO 取得した社員IDをもとに社員情報を削除する
		deleteEmployeeService.execute(empId);
		//  削除完了画面へリダイレクトする
		return "redirect:/delete/complete";
	}

	/**
	 * 社員情報の削除完了画面を出力
	 *
	 * @return 遷移先のビュー
	 */
	@RequestMapping(path = "/delete/complete", method = RequestMethod.GET)
	public String completeDelete() {
		//削除完了画面のビュー名を返す
		return "delete/delete_complete";

	}

}
