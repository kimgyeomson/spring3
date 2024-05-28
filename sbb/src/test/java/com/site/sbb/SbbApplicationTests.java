package com.site.sbb;

import com.site.sbb.answer.Answer;
import com.site.sbb.answer.AnswerRepository;
import com.site.sbb.question.Question;
import com.site.sbb.question.QuestionRepository;
import com.site.sbb.question.QuestionService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionService questionService;

	@Test
	void data_test() {
		for(int i = 1; i <= 300; i++) {
			String subject = String.format("테스트 데이터:[%03d]", i);
			String content = "내용 없음";
			this.questionService.create(subject, content, null);
		}
	}

	// 데이터를 통해 데이터 찾기 VS 데이터를
	@Transactional
	@Test
	void verses() {
		Optional<Question> oq = this.questionRepository.findById(10);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> answerlist = q.getAnswerList();

		assertEquals(1, answerlist.size());
		assertEquals("네 자동으로 생성됩니다.", answerlist.get(0).getContent());
	}

	//조회하기
	@Test
	void searchdata() {
		Optional<Answer> oa = this.answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(10, a.getQuestion().getId());

	}

	// 데이터 저장하기
	@Test
	void saveanswer() {
		Optional<Question> oq = this.questionRepository.findById(10);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q);
		a.setCreateTime(LocalDateTime.now());
		this.answerRepository.save(a);
	}

	// 데이터 삭제하기
	@Test
	void deletejpa() {
		assertEquals(2, this.questionRepository.count()); // 테이블 행의 개수를 리턴한다.
		Optional<Question> oq = this.questionRepository.findById(9);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		this.questionRepository.delete(q);
		assertEquals(1, this.questionRepository.count());
	}

	// 데이터 수정하기
	@Test
	void edit() {
		Optional<Question> oq = this.questionRepository.findById(9);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("수정된 제목");

		this.questionRepository.save(q);
	}

	// 특정 엔티티의 열 값들 중에 특정 문자열을 포함하는 데이터를 조회
	@Test
	void Likesubject() {
		List<Question> qList = questionRepository.findBySubjectLike("sbb%");
		Question q = qList.get(0);

		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	// subject & content 찾기
	@Test
	void findsubjectAndContent() {
		Question q = this.questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
		assertEquals(9, q.getId());
	}

	// 제목 찾기
	@Test
	void findBySubject() {
		Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
		assertEquals(9, q.getId());
	}

	// Id 조회
	@Test
	void testfindById() {
		Optional<Question> oq = this.questionRepository.findById(1);
		if(oq.isPresent()) {
			Question q = oq.get();
			assertEquals("sbb가 무엇인가요?", q.getSubject());
		}
	}

	// 질문 데이터 조회
	@Test
	void testfindJpa() {
		List<Question> all = this.questionRepository.findAll();
		assertEquals(2, all.size());

		Question q = all.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	// 질문 데이터 저장
	@Test
	void testJpa() {
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);

		Question q2 = new Question();
		q2.setSubject("스프링 부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2);
	}

}
