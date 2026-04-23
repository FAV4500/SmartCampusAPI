5COSC021W: Client-Server Architectures - Conceptual Report
Name: Ikponmwosa (Favour) Philips

Student ID: W1947505

Question 1: In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.

Answer: By default, JAX-RS resource classes are request-scoped, meaning the runtime creates a brand-new instance of the class for every single incoming request and discards it once the response is sent. It doesn't treat it as a singleton unless you specifically force it to with an annotation.
Because the class is re-instantiated every time, I can't just use a normal HashMap as a local variable inside the class, or the data would vanish after the first request. This is why I had to use a static data structure. However, since multiple requests can hit the server at the exact same time, there’s a risk of "race conditions" (where two requests try to update the map simultaneously). To prevent data loss or crashes, it’s essential to use thread-safe structures like ConcurrentHashMap or use synchronized blocks. This ensures that even though the resource instances are temporary, the shared in-memory data remains consistent and safe from corruption.

Question 2: Why is the provision of ”Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

Answer: Hypermedia is a big deal in REST because it follows the HATEOAS principle. Basically, it means the API sends back "next steps" as links within the JSON. Instead of a developer having to memorize every endpoint or constantly check a PDF manual, the API tells the client what it can do next based on the current state.
For a client-side developer, this makes life much easier. If the server changes a URL path, the client doesn't break because it’s just following a link provided at runtime rather than using a hardcoded string. It makes the app more flexible and less "coupled" to the specific structure of the server.

Question 3: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.

Answer: This is all about the balance between data size and the number of calls. If I only return IDs, the response is tiny, which saves a lot of network bandwidth. However, it's annoying for the client developer because if they want to display a simple list of room names, they have to make a separate API call for every single ID they received.
Returning full objects uses more data in one go, but it's usually better for the client because they get all the info they need in one "round trip." In a small project like this, full objects are better, but for a massive system with thousands of entries, returning just the IDs or a light summary is usually the professional way to do it.

Question 4: Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

Answer: My implementation isn't strictly idempotent. If you send the same DELETE request twice, the first one works and returns a success code (like 200). But the second time you send it, the room is already gone, so the code triggers a SmartException and returns a 404 Not Found.
Since the response code changes (from success to error), it doesn't fit the perfect definition of idempotency. That said, the "state" on the server stays the same after the first call—the room stays deleted—so it’s "effectively" idempotent even if the status codes don't stay the same.

Question 5: We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

Answer: If someone tries to send XML or plain text, JAX-RS stops them immediately. Because I used the @Consumes annotation for JSON, the server checks the "Content-Type" header of the request. If it doesn't match, the server automatically rejects it with a 415 Unsupported Media Type error.
This is great because it acts as a guard. It stops the application from trying to process the wrong data type, which would usually lead to a messy "500 Internal Server Error" or a crash during the unmarshalling process.

Question 6: You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path. Why is the query parameter approach generally considered superior for filtering and searching collections?

Answer: Using query parameters is much cleaner because the URL path is supposed to identify the "thing" (the resource), while parameters should "modify" the view. If you put filters in the path, like /type/CO2, it makes the URL structure very rigid.
If you want to filter by three different things at once, a path-based URL gets really confusing. With @QueryParam, you just append them. It’s much more flexible for the user because they can add or remove filters in any order without the server needing a special "path" for every possible combination.

Question 7: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path in one massive controller class?

Answer: This pattern stops you from having a "God Class" that contains every single piece of logic for the whole API. If I put every endpoint in one file, it becomes huge, messy, and really hard to test.
By delegating to sub-resources, the code is much more organized. The main class just finds the parent resource and then hands the rest of the work off to a specialized class. It makes the code modular, easier to read, and means you can fix a bug in "readings" without accidentally breaking the "sensors" logic.

Question 8: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

Answer: 422 (Unprocessable Entity) is better because it tells the developer exactly what went wrong. A 404 usually means the URL path is wrong or the page doesn't exist. If the URL is perfect and the JSON is valid, but an ID inside that JSON refers to something that doesn't exist, a 404 is misleading.
Using 422 tells the client: "I got your request and understood the format, but the data inside is logically wrong." It’s much more specific and helps the client developer fix their data rather than checking their URL strings.

Question 9: From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

Answer: Exposing a stack trace is like handing an attacker a blueprint of your house. It's a major security risk because it reveals internal details that are supposed to be hidden.
An attacker can see the exact versions of libraries (like Jersey or Jackson) I'm using, which lets them look up known vulnerabilities. They can also see absolute file paths on the server, the internal structure of my Java packages, and even clues about the database. By using an ExceptionMapper to hide these traces, I ensure that an attacker only gets a generic error message instead of a map of my server's weaknesses.

Question 10: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?

Answer: If I manually add logging to every method, my code gets cluttered with the same repetitive lines. If I want to change the log format later, I’d have to edit every single file.
Using a filter means I write the logging logic once, and it automatically applies to every request and response in the system. It’s more consistent, much easier to maintain, and keeps my resource classes focused on their actual job instead of being full of boilerplate code.