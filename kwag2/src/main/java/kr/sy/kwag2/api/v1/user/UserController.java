package kr.sy.kwag2.api.v1.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.sy.kwag2.common.constant.ResponseCode;
import kr.sy.kwag2.common.vo.ResponseVO;
import kr.sy.kwag2.common.vo.UserVO;
import kr.sy.kwag2.core.controller.BaseController;
import kr.sy.kwag2.core.service.UserService;

import java.util.List;

/**
 * 사용자 API Controller
 */
@RestController
@RequestMapping("/api/v1/users")
@Api(tags = "사용자 관리", description = "사용자 정보 관리 API")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 모든 사용자 조회
     */
    @GetMapping
    @ApiOperation(value = "모든 사용자 조회", notes = "모든 사용자 정보를 조회합니다")
    public ResponseEntity<ResponseVO<List<UserVO>>> getAll() {
        List<UserVO> users = userService.findAll();
        return ResponseEntity.ok(success(users));
    }

    /**
     * 사용자 단일 조회
     */
    @GetMapping("/{userId}")
    @ApiOperation(value = "사용자 단일 조회", notes = "사용자 ID로 사용자 정보를 조회합니다")
    public ResponseEntity<ResponseVO<UserVO>> getById(
            @ApiParam(value = "사용자 ID", required = true)
            @PathVariable String userId) {
        UserVO user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(error(ResponseCode.NOT_FOUND));
        }
        return ResponseEntity.ok(success(user));
    }

    /**
     * 사용자명으로 사용자 조회
     */
    @GetMapping("/search/username")
    @ApiOperation(value = "사용자명으로 조회", notes = "사용자명으로 사용자 정보를 조회합니다")
    public ResponseEntity<ResponseVO<UserVO>> getByUsername(
            @ApiParam(value = "사용자명", required = true)
            @RequestParam String username) {
        UserVO user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(error(ResponseCode.NOT_FOUND));
        }
        return ResponseEntity.ok(success(user));
    }

    /**
     * 이메일로 사용자 조회
     */
    @GetMapping("/search/email")
    @ApiOperation(value = "이메일로 조회", notes = "이메일로 사용자 정보를 조회합니다")
    public ResponseEntity<ResponseVO<UserVO>> getByEmail(
            @ApiParam(value = "이메일", required = true)
            @RequestParam String email) {
        UserVO user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(error(ResponseCode.NOT_FOUND));
        }
        return ResponseEntity.ok(success(user));
    }

    /**
     * 사용자 생성
     */
    @PostMapping
    @ApiOperation(value = "사용자 생성", notes = "새로운 사용자를 생성합니다")
    public ResponseEntity<ResponseVO<Void>> create(
            @ApiParam(value = "사용자 정보", required = true)
            @RequestBody UserVO userVO) {
        try {
            userService.create(userVO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(success(null, "사용자가 생성되었습니다"));
        } catch (Exception e) {
            logger.error("Error creating user", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(error(ResponseCode.BAD_REQUEST, e.getMessage()));
        }
    }

    /**
     * 사용자 수정
     */
    @PutMapping("/{userId}")
    @ApiOperation(value = "사용자 수정", notes = "사용자 정보를 수정합니다")
    public ResponseEntity<ResponseVO<Void>> update(
            @ApiParam(value = "사용자 ID", required = true)
            @PathVariable String userId,
            @ApiParam(value = "사용자 정보", required = true)
            @RequestBody UserVO userVO) {
        try {
            userVO.setUserId(userId);
            userService.update(userVO);
            return ResponseEntity.ok(success(null, "사용자가 수정되었습니다"));
        } catch (Exception e) {
            logger.error("Error updating user", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(error(ResponseCode.BAD_REQUEST, e.getMessage()));
        }
    }

    /**
     * 사용자 삭제
     */
    @DeleteMapping("/{userId}")
    @ApiOperation(value = "사용자 삭제", notes = "사용자를 삭제합니다")
    public ResponseEntity<ResponseVO<Void>> delete(
            @ApiParam(value = "사용자 ID", required = true)
            @PathVariable String userId) {
        try {
            userService.delete(userId);
            return ResponseEntity.ok(success(null, "사용자가 삭제되었습니다"));
        } catch (Exception e) {
            logger.error("Error deleting user", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(error(ResponseCode.BAD_REQUEST, e.getMessage()));
        }
    }
}