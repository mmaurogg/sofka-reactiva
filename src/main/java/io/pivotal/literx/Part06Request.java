package io.pivotal.literx;

import io.pivotal.literx.domain.User;
import io.pivotal.literx.repository.ReactiveRepository;
import io.pivotal.literx.repository.ReactiveUserRepository;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import org.slf4j.Logger;

/**
 * Learn how to control the demand.
 *
 * @author Sebastien Deleuze
 */
public class Part06Request {

	ReactiveRepository<User> repository = new ReactiveUserRepository();

	private static Logger log = LoggerFactory.getLogger(Part06Request.class);

	//========================================================================================

	// TODO Create a StepVerifier that initially requests all values and expect 4 values to be received
	StepVerifier requestAllExpectFour(Flux<User> flux) {

		return StepVerifier.create(flux)
				.expectNextCount(4)
				.expectComplete();
	}

//========================================================================================

	// TODO Create a StepVerifier that initially requests 1 value and expects User.SKYLER then requests another value and expects User.JESSE then stops verifying by cancelling the source
	StepVerifier requestOneExpectSkylerThenRequestOneExpectJesse(Flux<User> flux) {

		return StepVerifier.create(flux)
				.thenRequest(1)
				.expectNext(User.SKYLER)
				.thenRequest(1)
				.expectNext(User.JESSE)
				.thenCancel();
	}

//========================================================================================

	// TODO Return a Flux with all users stored in the repository that prints automatically logs for all Reactive Streams signals
	Flux<User> fluxWithLog() {

		return repository.findAll()
				.log();
	}

//========================================================================================

	// TODO Return a Flux with all users stored in the repository that prints "Starring:" on subscribe, "firstname lastname" for all values and "The end!" on complete
	Flux<User> fluxWithDoOnPrintln() {

		return repository.findAll()
				.doOnSubscribe(user -> System.out.println("Starring:"))
				.doOnNext(user -> System.out.println(user.getFirstname() + " " + user.getLastname()))
				.doOnComplete(() -> System.out.println("The end!"));

				//.doOnSubscribe(user -> log.info("Starring:"))
				//.doOnNext(user -> log.info(user.getFirstname() + " " + user.getLastname()))
				//.doOnComplete(() -> log.info("The end!"));
	}

}
