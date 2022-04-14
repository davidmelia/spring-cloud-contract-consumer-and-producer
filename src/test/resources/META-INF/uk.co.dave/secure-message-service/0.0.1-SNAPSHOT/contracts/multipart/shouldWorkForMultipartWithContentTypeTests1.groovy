import org.springframework.cloud.contract.spec.Contract

Contract.make {
	request {
		method 'POST'
		url '/tests1'
		multipart(
				[
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
		status 200
		body([
				status: 'ok'
		])
		headers {
			contentType('application/json')
		}
	}
}