import org.springframework.cloud.contract.spec.Contract

Contract.make {
	request {
		method 'POST'
		url '/api/1/customers/00000000000/accounts/0000000/tests1'
		multipart(
				[
		                subject: $(c(regex(nonEmpty())), p('subject')),
		                body: $(c(regex(nonEmpty())), p('body')),
		                topic: $(c(regex(nonEmpty())), p('GQ')),				
						files: named(
								name: value(consumer(regex(nonEmpty())), producer('filename1')),
								content: value(consumer(regex(nonEmpty())), producer('content1')))
				]
		)

		headers {
			contentType('multipart/form-data')
		}
	}
	response {
		status 201
	}
}
