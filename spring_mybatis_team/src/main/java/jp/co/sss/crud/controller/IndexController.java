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

import jp.co.sss.crud.form.LoginForm;
import jp.co.sss.crud.service.LoginResult;
import jp.co.sss.crud.service.LoginService;

@Controller
public class IndexController {

	@Autowired
	HttpSession session;

	@Autowired
	LoginService loginService;

	/**
	 * ログイン画面の表示
	 * 
	 * @param loginForm
	 * @return 遷移先ビュー
	 */
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String index(@ModelAttribute LoginForm loginForm) {
		session.invalidate();
		return "index";
	}

	/**
	 * ログインコントローラー
	 * 
	 * @param loginForm
	 * @param result エラー検知オブジェクト
	 * @param session 
	 * @param model リクエストスコープの操作
	 * @param user 
	 * @return 遷移先ビュー
	 */
	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public String login(@Valid @ModelAttribute LoginForm loginForm, BindingResult result, HttpSession session,
			Model model) {

		//TODO 入力エラーがある場合、result.hasErrorsメソッドを呼びだしindex.htmlへ戻る
		if (result.hasErrors()) {
			return "index";
		}

		//TODO loginServiceのメソッドを呼びだし、LoginResult型のオブジェクトへ代入する
		LoginResult loginResult = loginService.execute(loginForm);

		System.out.println("★");
		//TODO loginResult.isLoginの結果がtrueの場合、ログイン成功でセッションに"user"という名前でセッションにユーザーの情報を登録する
		if (loginResult.isLogin()) {

			//TODO セッションにuser登録
			session.setAttribute("user", loginResult.getLoginUser());

			// 一覧へリダイレクト
			return "redirect:/list";
			//TODO loginResult.isLoginの結果がfalseの場合、loginResult.getErrorMsgメソッドを呼びだし、modelスコープに登録する
		} else {//ログイン失敗時

			//TODO loginResult.getErrorMsgを呼び出し、メッセージをmodelスコープに登録
			model.addAttribute("errorMsg", loginResult.getErrorMsg());

			return "index";
		}

	}

	/**
	 * 
	 * ログアウト
	 * 
	 * @return 遷移先ビュー
	 */
	@RequestMapping(path = "/logout", method = RequestMethod.GET)
	public String logout() {
		//TODO セッションの破棄
		session.invalidate();

		//index.htmlへ遷移
		return "redirect:/";
	}

}
