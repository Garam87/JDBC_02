package edu.kh.jdbc1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc1.model.vo.Employee;

public class JDBCExample5 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 입사일을 입력("2022-09-06") 받아
		// 이름, 입사일, 성별(M, F) 조회
		Scanner sc = new Scanner(System.in);
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			System.out.print("입사일 입력(2022-09-06) : ");
			String inputDate = sc.next();
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String user = "kh";
			String pw = "kh1234";
			
			conn = DriverManager.getConnection(url, user, pw);
			
			stmt = conn.createStatement();
			
			String sql = "SELECT EMP_NAME 이름, TO_CHAR(HIRE_DATE, 'YYYY\"년\" MM\"월\" DD\"일\"') AS 입사일,"
					+ " DECODE(SUBSTR(EMP_NO, 8, 1), 1, 'M', 'F') AS 성별"
					+ " FROM EMPLOYEE"
					+ " WHERE HIRE_DATE < TO_DATE('" + inputDate + "')";
			
			// 문자열 내부에 쌍따옴표 작성 시 \" 로 작성해야 한다! (Escape 문자)
			
			rs = stmt.executeQuery(sql);
			
			List<Employee> list = new ArrayList<>(); // 조회 결과 저장용 List
			
			while(rs.next()) {
				
				Employee emp = new Employee(); // 기본 생성자로 Employee 객체 생성
												// 필드 초기화 x
												// setter를 이용해서 하나씩 세팅
				
				emp.setEmpName(rs.getString("이름"));
				emp.setHireDate(rs.getString("입사일"));
				emp.setGender(rs.getString("성별").charAt(0));
				
				
				
				// -> char 자료형 매개변수 필요
				// Java의 char : 문자 1개 의미
				// DB의 CHAR : 고정 길이 문자열(==String)
				// DB 컬럼 값을 char 자료형에 저장하고 싶으면
				// String.charAt(index) 이용!
				
				//String empId = rs.getString("이름");
				//String hireDate = rs.getString("HIRE_DATE");
				//String empNo = rs.getString("GENDER");
				
				// list에 emp 객체 추가
				list.add(emp);
				
				
			}
			
			// 조회 결과가 없는 경우
			if(list.size() == 0) {
				System.out.println("조회 결과가 없습니다.");
			} else {
				// 01 ) 선동일 / 1990년 02월 06일 / M
				// 일반 for문
				for(int i=0; i < list.size(); i++) {
					System.out.printf("%02d) %s / %s / %c\n",
							i+1,
							list.get(i).getEmpName(),
							list.get(i).getHireDate(),
							list.get(i).getGender()
							);
					
				}
			}
			
			
			
		} catch(Exception e) {
			System.out.println("오류남");
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch(SQLException e) {
				System.out.println("오류났다고!");
			}
		}
		
	}

}
