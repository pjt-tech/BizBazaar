# 멀티 테넌트 E-Commerce 플랫폼

## **설명**
이 플랫폼은 **멀티 테넌트 전자상거래 시스템**으로, 여러 판매자가 자신의 온라인 스토어를 운영할 수 있도록 지원합니다.  
판매자와 구매자를 위한 **별도의 인터페이스**를 제공하며, **상품 관리**, **주문 처리**, **결제 시스템**과 같은 기능을 지원합니다.  
최신 아키텍처 원칙을 적용하였으며, **DevOps 기술**을 활용하여 확장성, 안정성, 유지보수성을 보장합니다.

---

## **주요 기능**
- **판매자 인터페이스:**
  - 스토어 설정 및 커스터마이징.
  - 상품 재고 관리.
  - 주문 추적 및 분석 기능 제공.

- **구매자 인터페이스:**
  - 상품 검색 및 탐색.
  - 장바구니 및 위시리스트 기능.
  - 주문 및 결제 처리.

- **플랫폼 기능:**
  - 멀티 테넌트 아키텍처로 테넌트별 데이터 격리.
  - 중앙집중식 주문 및 결제 관리.
  - 강력한 인증 및 역할 기반 접근 제어.

---

## **사용 기술**
- **백엔드:**
  - Spring Boot를 활용한 REST API 개발.
  - JPA/Hibernate를 통한 데이터베이스 관리.

- **프론트엔드:**
  - Thymeleaf를 이용한 서버 사이드 렌더링.

- **보안:**
  - JWT 기반 인증 및 권한 관리.
  - 판매자, 구매자, 관리자 역할별 접근 제어.

- **DevOps:**
  - Docker를 활용한 컨테이너화.
  - Kubernetes를 통한 배포 오케스트레이션.
  - Jenkins/GitHub Actions를 사용한 CI/CD 파이프라인.
  - AWS/GCP 클라우드 인프라 활용.

---

## **아키텍처**
- **멀티 테넌트 모델:**
  - 각 판매자는 독립된 환경에서 운영.
  - 단일 데이터베이스에서 테넌트별 스키마 관리.

- **서비스 지향 설계:**
  - 주문, 상품, 결제를 각각 독립된 마이크로서비스로 분리.
  - API 게이트웨이를 통한 통합 접근 제어.

- **확장성:**
  - 로드 밸런싱 및 자동 확장 지원.
  - Redis 캐싱을 통한 성능 최적화.

---

## **시작하기**
### 사전 준비
- Java 17 이상
- Docker 및 Docker Compose
- MariaDB

### 설치 및 실행
1. 레포지토리 클론:
   ```bash
   git clone https://github.com/your-repo/multi-tenant-ecommerce.git