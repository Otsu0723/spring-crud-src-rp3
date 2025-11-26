package jp.co.sss.crud.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.sss.crud.entity.Employee;

/**
 * 権限認証用フィルタ
 * 
 * @author System Shared
 */
public class AdminAccountCheckFilter extends HttpFilter {

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// URIと送信方式を取得する
		String requestURI = request.getRequestURI();
		String requestMethod = request.getMethod();

		// 完了画面はフィルターを通過させる
		if (requestURI.contains("/complete") && requestMethod.equals("GET")) {
			chain.doFilter(request, response);
			return;
		}

		//TODO セッションからユーザー情報を取得
		HttpSession session = request.getSession(false);
		Object sessionUser = (session != null) ? session.getAttribute("user") : null;

		//TODO セッションユーザーのIDと権限の変数をそれぞれ初期化
		Integer loginEmpId = null;
		Integer loginAuthority = null;

		//TODO セッションユーザーがNULLでない場合
		if (sessionUser != null) {
			//TODO セッションユーザーからID、権限を取得して変数に代入
			Employee user = (Employee) sessionUser;
			loginEmpId = user.getEmpId();
			loginAuthority = user.getAuthority();
		}

		//TODO  更新対象の社員IDをリクエストから取得
		String empIdStr = request.getParameter("empId");

		// パスパラメータ（/update/3など）の場合にも対応
		if (empIdStr == null) {
			String[] split = requestURI.split("/");
			String last = split[split.length - 1];
			if (last.matches("\\d+")) {
				empIdStr = last;
			}
		}

		// 変換後のID格納用
		Integer targetEmpId = null;

		//TODO  社員IDがNULLでない場合
		if (empIdStr != null) {
			//TODO 社員IDを整数型に変換
			targetEmpId = Integer.parseInt(empIdStr);
		}

		//フィルター通過のフラグを初期化 true:フィルター通過 false:ログイン画面へ戻す
		boolean accessFlg = false;

		//TODO  管理者(セッションユーザーのIDが2)の場合、アクセス許可
		if (loginEmpId != null && loginEmpId == 2) {
			accessFlg = true;
			//TODO  ログインユーザ自身(セッションユーザのIDと変更リクエストの社員IDが一致)の画面はアクセス許可
		} else if (loginEmpId != null && targetEmpId != null && loginEmpId.equals(targetEmpId)) {
			accessFlg = true;
		}

		//TODO  accessFlgが立っていない場合はログイン画面へリダイレクトし、処理を終了する
		if (!accessFlg) {
			//TODO  レスポンス情報を取得
			response.sendRedirect(request.getContextPath() + "/");
			//TODO  ログイン画面へリダイレクト

			//処理を終了
			return;
		}

		chain.doFilter(request, response);
		return;

	}

}
