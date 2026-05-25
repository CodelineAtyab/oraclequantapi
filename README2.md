Edge Cases & Unexpected Behavior


Case 1: cba → [3]
c = 3 → header: expects 3 items
b = 2 → item 1
a = 1 → item 2
input exhausted before item 3 — decoder sums what it has
packet total = 2+1 = 3
Note: Header promised 3 items but only 2 were available. Decoder is lenient — no exception thrown.


Case 2: az → [26]
a = 1 → header: expects 1 item
z = 26 → continuation flag, but input ends here
decoder treats z as a complete value of 26
packet total = 26
Note: z normally requires a following character to terminate. When input ends after z, decoder accepts 26 as-is.


Case 3: _a → [0, 0]
_ = 0 → header: expects 0 items
packet 1 total = 0

a = 1 → header: expects 1 item
input exhausted — no items available
packet 2 total = 0
Note: Header with no items following it produces 0.


Summary
The decoder never throws an exception on malformed input. It gracefully handles truncated streams, dangling z continuations, and unfulfilled item counts by summing whatever is available.